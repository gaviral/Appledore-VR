-- This information is used by the Wi-Fi dongle to make a wireless connection to the router in the Lab
SSID = "M112-PD"
--SSID = "G4"
--SSID = "SM-G920W85778"
SSID_PASSWORD = "aiv4aith2Zie4Aeg"
--SSID_PASSWORD = "aviral123"
--SSID_PASSWORD = "fvlu3385"

-- configure ESP as a station
wifi.setmode(wifi.STATION)
wifi.sta.config(SSID,SSID_PASSWORD)
wifi.sta.autoconnect(1)
