Intent Scheme Hijacking Vulnerability

This document is written for the explanation about the warning shows on google play when google found a vulnerability about intent scheme hijacking. Google document shows two options for suppressing the warning.

https://support.google.com/faqs/answer/9101196
General Issue of the HTTP Vulnerability
Detail: 
Why can’t we open http server with WebView? 
Actually, HTTP server is a variant of the FTP server. Client sends Request Message to HTTP Server(Like apache,nginx,tomcat,jetty…). 

`GET / HTTP1.1`

Then HTTP Server responds and sends back HTML file to the client. 
All of the transactions are performed with clear text.  So, http protocol is vulnerable to malicious attacks (Like sniffing or spoofing/Man in the middle attack) .

So we usually use HTTPS for using the HTTP protocol. Because it encrypts these protocols with SSL(e.g. for posting the web forms). But nowadays, web browsers have started to regard all HTTP connections as vulnerable. So chrome shows alert when connecting to the address which starts from `http` recently like below.



This is the reason why we can’t open some url in android webview after SDK28(Android 9).
Setting up your test environment:
You can easily setup your web browser with some tools. (e.g. chrome plugin or nginx or node.js)
If you want to learn about web systems, you better set up a http server or http application will be good. But if not, you can easily launch a web server using android studio. Put some html file in your project and right click it then open with chrome. Then you can get a live coding url...
Conclusion : 
This chapter has explained to you a general issue of the HTTP. 
The issue is related to Intent scheme vulnerability, because we can write intent scheme in html file as a link tag. So every untrusted server should not be shown on the webview in android app. This is the reason why we received a warning.

<a href="intent://wksample.com/samples/1#Intent;package=com.example.webkitsample;category=android.intent.category.BROWSABLE;action=android.intent.action.VIEW;scheme=https;end;">

Solution:
If you want to release your app for production, you should prepare a proper https server. But if you can’t handle it yourself (e.g. tmdb not supports https api). You can change the default setting of webview in AndroidManifest.xml. Append a below attribute within the application tag.

android:usesCleartextTraffic="true"

Also, you can use the `res/xml/network_security_config.xml` file for it.


Trojan App Attack 

Then we will look into option 2 in the google help document. In short ,Google recommends us to use `Implicit Intent` without option for opening HTTP URL in this paragraph.
Detail: 
Implicit Intent resolves app from given url. If you tell HTTP url to the resolver, it will return the list of apps which can handle http protocol. (E.g. Chrome, Firefox, Opera, Samsung web browser.)
Then the user opens the url with the selected app.  This is the intended behaviour.

  // convert Intent scheme URL to Intent object
  Intent intent = Intent.parseUri(url);
  // forbid launching activities without BROWSABLE category
  intent.addCategory("android.intent.category.BROWSABLE");
  // forbid explicit call
  intent.setComponent(null);
  // forbid Intent with selector Intent
  intent.setSelector(null);
  // start the activity by the Intent
  view.getContext().startActivity(intent, -1);

However, what if we use explicit intent in that case? It also looks fine, and explicit intent could designate an app and even an activity developers want to open.  But actually it causes the problem.

There were some malicious apps that launched specific web browsers with explicit intent. with malicious html/javascript, then stole cookies or some secure information with harmful html/javascript, via calling private activity.
(https://www.mbsd.jp/Whitepaper/IntentScheme.pdf).

So, Explicit Intents which launch web browsers are not permitted to be used in app.

