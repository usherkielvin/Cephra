# TODO for Monitor Fullscreen Fixes

## Information Gathered
- Fullscreen mode implemented with header containing theme and language controls
- fullscreenThemeBtn calls themeBtn.click() but themeBtn is hidden in fullscreen, causing functionality issues
- fullscreenLangBtn is a button that calls languageBtn.click(), which doesn't change language since languageBtn is a select
- Need to convert fullscreenLangBtn to a select dropdown with same options as main languageBtn
- Theme toggle logic needs to be directly implemented for fullscreenThemeBtn

## Plan
- [x] Modify createFullscreenUI() to create fullscreenLangBtn as a select element with language options
- [x] Update fullscreenLangBtn event handler to change language directly using same logic as main languageBtn
- [x] Change fullscreenThemeBtn onclick to directly toggle theme using the same logic as themeBtn.onclick
- [x] Ensure fullscreen controls update main UI state and localStorage

## Dependent Files to Edit
- Appweb/Monitor/index.php (JavaScript section)

## Followup Steps
- [x] Test fullscreen mode activation
- [x] Test theme toggle button in fullscreen
- [x] Test language dropdown in fullscreen
- [x] Verify theme and language changes sync between fullscreen and main UI
- [x] Check localStorage persistence for both theme and language preferences
