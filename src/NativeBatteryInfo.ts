// ==========================
// BatteryInfo TurboModule
// ==========================

import {
  NativeEventEmitter,
  type NativeModule,
  TurboModuleRegistry,
  type TurboModule,
} from "react-native";

import type { batteryInfoType } from "./types/batteryinfoType";

// --------------------------
// TurboModule Spec
// --------------------------
export interface Spec extends TurboModule {
  getBatteryInfo(): Promise<batteryInfoType>;
  addListener(eventName: string): void;
  removeListeners(count: number): void;
}

// --------------------------
// Module Instance
// --------------------------
const NativeBattery = TurboModuleRegistry.getEnforcing<Spec>("BatteryInfo");

// --------------------------
// Event Emitter
// --------------------------
export const EventNativeBattery = new NativeEventEmitter(
  NativeBattery as unknown as NativeModule
);

// --------------------------
// Exports
// --------------------------
export default NativeBattery;
