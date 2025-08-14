// ==========================
// BatteryInfo Index Exports
// ==========================

// Hooks
export { useBatteryInfo } from "./hook/useBatteryInfo";

// Types
export type { batteryInfoType, batteryEmitterType } from "./types/batteryinfoType";

// Native Module
export {
	default as NativeBattery,
	EventNativeBattery,
} from "./NativeBatteryInfo";