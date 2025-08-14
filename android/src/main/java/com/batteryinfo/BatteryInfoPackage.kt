package com.batteryinfo

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.NativeModule
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

class BatteryInfoPackage : TurboReactPackage() {
    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        return if (name == BatteryInfoModule.NAME) {
            BatteryInfoModule(reactContext)
        } else null
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
        return ReactModuleInfoProvider {
            mapOf(
                BatteryInfoModule.NAME to ReactModuleInfo(
                    BatteryInfoModule.NAME,
                    BatteryInfoModule.NAME,
                    false,  // canOverrideExistingModule
                    false,  // needsEagerInit
                    true,   // hasConstants
                    false,  // isCxxModule
                    true    // isTurboModule
                )
            )
        }
    }
}
