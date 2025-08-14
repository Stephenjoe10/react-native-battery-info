// ==========================
// useBatteryInfo Hook
// ==========================

import { useEffect, useState } from "react";
import NativeBattery, { EventNativeBattery } from "../NativeBatteryInfo";
import type {
	batteryEmitterType,
	batteryInfoType,
} from "../types/batteryinfoType";

// --------------------------
// Hook: useBatteryInfo
// --------------------------
export const useBatteryInfo = () => {
	const [staticData, setStaticData] = useState<batteryInfoType>();
	const [dynamicData, setDynamicData] = useState<batteryEmitterType>();

	// Fetch static info once and subscribe to dynamic updates
	useEffect(() => {
		fetchStaticInfo();

		const subscription = EventNativeBattery?.addListener(
			"OnBatteryChange",
			(response) => {
				setDynamicData(response);
			}
		);

		return () => {
			subscription?.remove();
		};
	}, []);

	// --------------------------
	// Fetch battery static info
	// --------------------------
	const fetchStaticInfo = async () => {
		try {
			const info = await NativeBattery?.getBatteryInfo();
			setStaticData(info);
		} catch (error) {
			console.error("Failed to get battery info:", error);
		}
	};

	// Merge static and dynamic data
	return { ...staticData, ...dynamicData };
};