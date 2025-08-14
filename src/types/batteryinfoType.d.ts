// ==========================
// Battery Info Types
// ==========================

// --------------------------
// Static Battery Info
// --------------------------
export type batteryInfoType = {
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
};

// --------------------------
// Dynamic Battery Info (Emitter)
// --------------------------
export type batteryEmitterType = {
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
};
