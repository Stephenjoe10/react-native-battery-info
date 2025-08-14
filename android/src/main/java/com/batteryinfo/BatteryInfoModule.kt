package com.batteryinfo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

class BatteryInfoModule(reactContext: ReactApplicationContext):NativeBatteryInfoSpec(reactContext) {
    override fun getName() = NAME

    private val batteryReceiver = object:BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            if(intent==null) return

            val bm = reactApplicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

            val map:WritableMap = Arguments.createMap()
            val batteryProperty:WritableMap = Arguments.createMap()
            val batteryStatus:WritableMap = Arguments.createMap()
            val batteryPlugged:WritableMap = Arguments.createMap()
            val extra:WritableMap = Arguments.createMap()

            //Battery Property
            batteryProperty.putInt("chargeCounter",bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER))
            batteryProperty.putInt("currentAverage",bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE))
            batteryProperty.putInt("currentNow",bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW))
            batteryProperty.putInt("energyCounter",bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER))

            //charging Status
            val status =  intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1)
            batteryStatus.putBoolean("isCharging",status == BatteryManager.BATTERY_STATUS_CHARGING)
            batteryStatus.putBoolean("isChargeFull",status == BatteryManager.BATTERY_STATUS_FULL)
            batteryStatus.putBoolean("isDischarging",status == BatteryManager.BATTERY_STATUS_DISCHARGING)

            //plugged Type
            val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1)
            batteryPlugged.putBoolean("isAc",plugged == BatteryManager.BATTERY_PLUGGED_AC)
            batteryPlugged.putBoolean("isUsb",plugged == BatteryManager.BATTERY_PLUGGED_USB)
            batteryPlugged.putBoolean("isWireless",plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS)

            //Extra
            intent.let {
                extra.putInt("level",it.getIntExtra(BatteryManager.EXTRA_LEVEL,-1))
                extra.putInt("voltage",it.getIntExtra(BatteryManager.EXTRA_VOLTAGE,-1))
                extra.putInt("temperature",it.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1))
                extra.putBoolean("isPresent",it.getBooleanExtra(BatteryManager.EXTRA_PRESENT,false))

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    extra.putBoolean(
                        "isBatteryLow",
                        it.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW,false)
                    )
                }
                val iconId = it.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0)
                val iconName = if(iconId !=0){
                    try{
                        reactApplicationContext.resources.getResourceEntryName(iconId)
                    }catch (e:Exception){
                        null
                    }
                }else null
                extra.putString("iconName",iconName)
            }

            map.putBoolean("isCharging",bm.isCharging)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val chargeTimeRemaining = bm.computeChargeTimeRemaining()
                map.putDouble("chargeTimeRemaining", chargeTimeRemaining.toDouble())
            }
            val batteryCapacity = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            map.putInt("currentPercentage",batteryCapacity)
            map.putMap("extra",extra)
            map.putMap("batteryProperty",batteryProperty)
            map.putMap("batteryStatus",batteryStatus)
            map.putMap("batteryPlugged",batteryPlugged)
            sendEvent("OnBatteryChange",map)
        }
    }
    override fun initialize() {
        super.initialize()
        reactApplicationContext.registerReceiver(
            batteryReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
    }

    override fun invalidate() {
        super.invalidate()
        reactApplicationContext.unregisterReceiver(batteryReceiver)
    }

    @Suppress("SameParameterValue")
    private  fun sendEvent(eventName:String, params:WritableMap){
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName,params)
    }

    override fun getBatteryInfo(promise: Promise?) {
     try {
         val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
         val intent = reactApplicationContext.registerReceiver(null,filter)

         val map:WritableMap = Arguments.createMap()

         val healthStatus = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH,-1)?:-1

         val health:WritableMap = Arguments.createMap()
         health.putBoolean("isGood",healthStatus== BatteryManager.BATTERY_HEALTH_GOOD)
         health.putBoolean("isCold",healthStatus== BatteryManager.BATTERY_HEALTH_COLD)
         health.putBoolean("isOverHeat",healthStatus== BatteryManager.BATTERY_HEALTH_OVERHEAT)
         health.putBoolean("isDead",healthStatus== BatteryManager.BATTERY_HEALTH_DEAD)
         health.putBoolean("isOverVoltage",healthStatus== BatteryManager.BATTERY_HEALTH_OVERHEAT)
         health.putBoolean("isGood",healthStatus== BatteryManager.BATTERY_HEALTH_GOOD)
         health.putBoolean("isFailure",healthStatus== BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE)
         health.putBoolean("isGood",healthStatus== BatteryManager.BATTERY_HEALTH_GOOD)

         val batteryTechnology = intent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
         val batteryScale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE,-1)?:-1

         map.putMap("batteryHealth",health)
         map.putString("batteryTechnology",batteryTechnology)
         map.putInt("batteryScale",batteryScale)

         promise?.resolve(map)
     }
     catch (error:Exception){
         promise?.reject(error)
     }
    }

    override fun addListener(eventName: String?) {

    }

    override fun removeListeners(count: Double) {

    }

    companion object {
        const val NAME = "BatteryInfo"
    }
}
