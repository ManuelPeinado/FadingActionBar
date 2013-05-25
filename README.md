FadingActionBar
==================

FadingActionBar is a library which implements the cool fading action bar effect that can be seen in the new Play Music app.

This library uses the techniques outlined by Cyril Mottier in [a recent blog post][1].

For reasons of backwards compatibility this library relies on ActionBarSherlock, but it should be really easy to port it to the native action bar. 

![Example Image][2]

Try out the sample application:

<a href="https://play.google.com/store/apps/details?id=com.manuelpeinado.fadingactionbar.demo">
  <img alt="Android app on Google Play"
       src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

Or browse the [source code of the sample application][3] for a complete example of use.

Including in your project
-------------------------

Just add the library to your application as a library project. And don't forget that your project must use ActionBarSherlock!

Maven integration will come soon.

Usage
---------

Using the library is really simple, just look at the source code of the provided samples:

* [If your content should be in a `ScrollView`][4].
* [If your content should be in a `ListView`][5].

Tutorials for both scenarios are in the works.



Acknowledgements
--------------------

* Thanks to Cyril Mottier for sharing the techniques that make this library possible.


Who's using it
--------------

*Does your app use FadingActionBar? If you want to be featured on this list drop me a line.*


Developed By
--------------------

Manuel Peinado Gallego - <manuel.peinado@gmail.com>

<a href="https://twitter.com/mpg2">
  <img alt="Follow me on Twitter"
       src="https://raw.github.com/ManuelPeinado/NumericPageIndicator/master/art/twitter.png" />
</a>
<a href="https://plus.google.com/106514622630861903655">
  <img alt="Follow me on Google+"
       src="https://raw.github.com/ManuelPeinado/NumericPageIndicator/master/art/google-plus.png" />
</a>
<a href="http://www.linkedin.com/pub/manuel-peinado-gallego/1b/435/685">
  <img alt="Follow me on LinkedIn"
       src="https://raw.github.com/ManuelPeinado/NumericPageIndicator/master/art/linkedin.png" />


License
-----------

    Copyright 2013 Manuel Peinado

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.




 [1]: http://cyrilmottier.com/2013/05/24/pushing-the-actionbar-to-the-next-level/
 [2]: https://raw.github.com/ManuelPeinado/FadingActionBar/master/art/readme_pic.png
 [3]: https://github.com/ManuelPeinado/FadingActionBar/tree/master/sample
 [4]: https://github.com/ManuelPeinado/FadingActionBar/blob/master/sample/src/com/manuelpeinado/fadingactionbar/demo/ScrollViewActivity.java
 [5]: https://github.com/ManuelPeinado/FadingActionBar/blob/master/sample/src/com/manuelpeinado/fadingactionbar/demo/ListViewActivity.java

