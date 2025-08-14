# 📱 React Native BatteryInfo TurboModule

A lightweight **React Native TurboModule** to get **static** and **dynamic** battery information directly from the native layer.  
Supports listening to battery status changes in real time.

---

## ✨ Features

- 🔋 **Static battery info** – Technology, scale, health, etc.
- ⚡ **Dynamic battery info** – Charge status, temperature, voltage, percentage, etc.
- 🎧 **Event listener** for battery state changes
- 📦 Built with **React Native TurboModule** for high performance

---

## 📦 Installation

```sh
npm install react-native-battery-info
# or
yarn add react-native-battery-info
```

---

## 🛠 Usage

### 1️⃣ Using the Hook (Recommended for UI)
The `useBatteryInfo` hook merges static and dynamic battery data automatically and re-renders when values change.

```tsx
import { useBatteryInfo } from "react-native-battery-info";
import { View, Text } from "react-native";

export default function BatteryInfoView() {
  const battery = useBatteryInfo();

  return (
    <View>
      <Text>Battery %: {battery.currentPercentage}</Text>
      <Text>Charging: {battery.isCharging ? "Yes" : "No"}</Text>
      <Text>Temperature: {battery.extra?.temperature}°C</Text>
      <Text>Voltage: {battery.extra?.voltage} mV</Text>
    </View>
  );
}
```

---

### 2️⃣ Using EventEmitter Directly (For Advanced Control)
Use `EventNativeBattery` when you want to manually subscribe/unsubscribe from updates.

```tsx
import { useEffect } from "react";
import { EventNativeBattery, NativeBattery } from "react-native-battery-info";

export default function BatteryListener() {
  useEffect(() => {
    // Subscribe to real-time updates
    const subscription = EventNativeBattery.addListener(
      "OnBatteryChange",
      (data) => {
        console.log("Dynamic Battery Data:", data);
      }
    );

    // Fetch static info once
    (async () => {
      const staticInfo = await NativeBattery.getBatteryInfo();
      console.log("Static Battery Info:", staticInfo);
    })();

    // Cleanup
    return () => subscription.remove();
  }, []);

  return null;
}
```

---

## 🔍 API Types

### **Static Data** (`batteryInfoType`)
```ts
{
  batteryTechnology: string;
  batteryScale: number;
  batteryHealth: {
    isFailure: boolean;
    isOverVoltage: boolean;
    isDead: boolean;
    isOverHeat: boolean;
    isCold: boolean;
    isGood: boolean;
  };
}
```

### **Dynamic Data** (`batteryEmitterType`)
```ts
{
  batteryProperty: {
    energyCounter: number;
    currentNow: number;
    currentAverage: number;
    chargeCounter: number;
  };
  batteryPlugged: {
    isWireless: boolean;
    isUsb: boolean;
    isAc: boolean;
  };
  batteryStatus: {
    isChargeFull: boolean;
    isDischarging: boolean;
    isCharging: boolean;
  };
  extra: {
    isBatteryLow: boolean;
    iconName: string;
    temperature: number;
    level: number;
    voltage: number;
    isPresent: boolean;
  };
  currentPercentage: number;
  chargeTimeRemaining: number;
  isCharging: boolean;
}
```