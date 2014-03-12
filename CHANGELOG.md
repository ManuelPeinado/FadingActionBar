Change Log
=======================================

Version 3.1.2 *(2014-03-12)*
----------------------------
 * Fill available space when content too small. Prompted by this [SO question](http://stackoverflow.com/questions/22332696/fadingactionbar-linearlayout-does-not-match-parent).


Version 3.1.1 *(2014-01-25)*
----------------------------
 * Remove actionbar shadow from samples.
 * Library not modified, version not uploaded to maven central.


Version 3.1.0 *(2014-01-19)*
----------------------------
 * Added support for WebView content.

Version 3.0.0 *(2013-12-04)*
----------------------------
 * Revamped project structure, now all AB types (stock, sherlock, compat) are under the same repo. Stock AB support in in the main library, whereas other types are in the "extras" folder.
* Adds support for header overlays
* Fixes bug when content is ListView and notifyDataSetChange() is called

Version 2.2.2 *(2013-07-13)*
----------------------------
* Fixes yet another severe issue in handling of ListViews (https://github.com/ManuelPeinado/FadingActionBar/issues/6)

Version 2.2.1 *(2013-06-22)*
----------------------------
* Fixes severe issue (https://github.com/ManuelPeinado/FadingActionBar/issues/6)
* Adds support for HoloEverywhere (https://github.com/ManuelPeinado/FadingActionBar/issues/4).

Version 2.1.0 *(2013-06-01)*
----------------------------
* Added support for parallax scroll
* Added new demo that showcases integration with DrawerLayout

Version 2.0.1 *(2013-05-29)*
----------------------------
 * Use getHeight() instead of getMeasuredHeight()

Version 2.0.0 *(2013-05-28)*
----------------------------
 * New fluent API for initialization
 * Added support for fragments
 * Added support for light action bars
 * Added maven support
 
Version 1.0.2 *(2013-05-26)*
----------------------------
 * Added callback to the actionbar background drawable. This should fix a bug that prevents the app from working properly in some devices.

Version 1.0.1 *(2013-05-25)*
----------------------------

 * Added longer content to the ScrollView sample so that it is scrollable in tablets.

Version 1.0 *(2013-05-25)*
----------------------------
Initial release.
