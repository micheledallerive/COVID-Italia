# COVID Italia

Android application to track the COVID-19 pandemic in Italy, using the official data provided by the [Italian Civil Protection Department](https://github.com/pcm-dpc/COVID-19).

The app is written in **Java**, using the **Android SDK**. It implements the **MVVM** architecture and common design patterns such as Singleton, Adapter, Observer, Factory, etc.

## Screenshots

<style>.imgc{max-width: 20%; min-width: 150px; width: clamp(150px, 25%, 300px); text-align: center; gap: 8px; font-size: clamp(11px, 1.5vw, 14px)}</style>
<div style="display: flex; flex-direction: row; flex-wrap: wrap; gap: 2em; justify-content: space-around; margin-top: 16px">
    <div class="imgc" style="flex-direction: column; align-items:center; display:flex;">
        <img src="./screenshots/loading.png" alt="Loading page">
        <p>Loading screen with prevention suggestion.</p>
    </div>
    <div class="imgc" style="flex-direction: column; align-items:center; display:flex;">
        <img src="./screenshots/map.png" alt="Loading page">
        <p>Interactive map with daily data of each region. The regions are colored depending on the number of positive cases.</p>
    </div>
    <div class="imgc" style="flex-direction: column; align-items:center; display:flex;">
        <img src="./screenshots/positive.png" alt="Loading page">
        <p>Charts display the data of positive, healed and dead people during time. <br/>The region of the data can be chosen.</p>
    </div>
    <div class="imgc" style="flex-direction: column; align-items:center; display:flex;">
        <img src="./screenshots/daily.png" alt="Loading page">
        <p>Daily section shows the overral data change in the previous 24 hours.</p>
    </div>
    <div class="imgc" style="flex-direction: column; align-items:center; display:flex;">
        <img src="./screenshots/news.png" alt="Loading page">
        <p>News section with recent health news from the biggest newspapers in Italy.</p>
    </div>
</div>