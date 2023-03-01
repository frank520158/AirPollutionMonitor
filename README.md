# AirPollutionMonitor

AirPollutionMonitor is an application to monitor air pollution from [API of Environmental Protection Administration (EPA) in Taiwan](https://data.epa.gov.tw/api/v2/aqx_p_432?limit=1000&api_key=cebebe84-e17d-4022-a28f-81097fda5896&sort=ImportDate%20desc&format=json).

### Banner List ###
Show data with PM2.5 <= 30 in horizontal.  
### Air List ###
Show data with PM2.5 > 30 in vertical.  
When status = "良好", show "The status is good, we want to go out
to have fun".  
When status != "良好", show button and show toast when pressing it.  
### Search feature ###
Filter all air data with keyword and show on vertical list.

## Used Technique ##
- Kotlin
- MVVM
- LiveData
- Coroutine
- Retrofit
- ViewBinding
- Navigation
- Sealed Class
- Koin
