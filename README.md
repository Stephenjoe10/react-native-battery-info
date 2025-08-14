# 📱 React Native BatteryInfo TurboModule

A lightweight **React Native TurboModule** to get **static** and **dynamic** battery information directly from the native layer.  
Supports listening to battery status changes in real time.

---

## 📰 News
> **Note:** This package is currently available **only for Android**.  
> iOS support is planned for future releases.

---

## ✨ Features

- 🔋 **Static battery info** – Technology, scale, health, etc.
- ⚡ **Dynamic battery info** – Charge status, temperature, voltage, percentage, etc.
- 🎧 **Event listener** for battery state changes
- 📦 Built with **React Native TurboModule** for high performance

---

## 📦 Installation

```sh
npm install react-native-battery-info-pro
# or
yarn add react-native-battery-info-pro
```

---

## 🛠 Usage

### 1️⃣ Using the Hook (Recommended for UI)
The `useBatteryInfo` hook merges static and dynamic battery data automatically and re-renders when values change.

```tsx
import { useBatteryInfo } from "react-native-battery-info-pro";
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
listener event name `OnBatteryChange`

```tsx
import { useEffect } from "react";
import { EventNativeBattery } from "react-native-battery-info-pro";
import type { batteryEmitterType } from "../types/batteryinfoType";

export default function BatteryListener() {
  useEffect(() => {
    // Subscribe to real-time updates
    const subscription = EventNativeBattery.addListener(
      "OnBatteryChange",
      (data:batteryEmitterType) => {
        console.log("Dynamic Battery Data:", data);
      }
    );

    // Cleanup
    return () => subscription.remove();
  }, []);

  return null;
}
```

---

### 3️⃣ Using Static battery info api (Recommended for static info only)
The `getBatteryInfo` api is used to fetch the static battery info data.

```tsx
import { NativeBattery } from "react-native-battery-info-pro";
import { View, Text } from "react-native";
import type { batteryInfoType } from "../types/batteryinfoType";

export default function BatteryStaticInfoView() {
  const [staticData, setStaticData] = useState<batteryInfoType>();

  useEffect(() => {
		  fetchStaticInfo();
	}, []);

// Fetch static info once
  const fetchStaticInfo = async () => {
		try {
			const info = await NativeBattery?.getBatteryInfo();
			setStaticData(info);
		} catch (error) {
			console.error("Failed to get battery info:", error);
		}
	};

  return (
    <View>
      <Text>Battery health is Good: {staticData?.batteryHealth?.isGood ? "yes":"no"}</Text>
      <Text>Battery Technology: {staticData?.batteryTechnology}</Text>
      <Text>Battery Scale: {staticData?.batteryScale}</Text>
    </View>
  );
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