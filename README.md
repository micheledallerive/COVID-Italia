# COVID Italia

Android application to track the COVID-19 pandemic in Italy, using the official data provided by the [Italian Civil Protection Department](https://github.com/pcm-dpc/COVID-19).

The app is written in **Java**, using the **Android SDK**. It implements the **MVVM** architecture and common design patterns such as Singleton, Adapter, Observer, Factory, etc.

## Features

- **Data visualization**: the app shows the data provided by the Italian Civil Protection Department in a clear and intuitive way, using charts and an interactive map with different colors to indicate the number of cases. The map is created using [Bing Maps SDK](https://www.bingmapsportal.com/).

- **Data update**: the app automatically downloads the latest data from the official repository, and updates the data visualization.

- **Notifications**: the app can send notifications to the user, to inform him about the latest data as soon as it is released.

- **News**: the app shows the latest news about the COVID-19 pandemic in Italy, retrieved using the RSS Feed of the biggest Italian newspapers.


## Screenshots

<img src="./screenshots/loading.png" alt="Loading page" width=30%> &nbsp;
<img src="./screenshots/map.png" alt="Loading page" width=30%> &nbsp;
<img src="./screenshots/positive.png" alt="Loading page" width=30%> 
<img src="./screenshots/daily.png" alt="Loading page" width=30%> &nbsp;
<img src="./screenshots/news.png" alt="Loading page" width=30%> &nbsp;