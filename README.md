#FireBase Overview
Firebase is a mBaas/Baas/Server-less environment. BackEnd as a service.
What is BackEnd? 

We need a lot of applications/systems when we want to develop a web-based system. 
the system especially running behind frontend application. 

Database / ContentServer / ApplicationServer / Authorization / Caching / Scheduled Task / Task Queue

FireBase aggregates the functions that we need for the BackEnd system. That is especially designed for the Mobile Application Backend. (Recently they started to support web systems.)

Also they are providing solutions for testing and growth. So products provided by firebase are categorized by these 4 blocks.

Develop - These products provide the Backend system itself. They have elastic authentication system/Realtime database product/CDN Storage/Cloud Functions/ML Kit/
Quality - These products are used for development and distribution. Like supporting tests, analyzing code or something. Crashlytics could gather crash information and stack trace from each user’s installed your app.
Analytics - These products are used for the growth of a partner's business. Formally the products are provided by a part of Google Analytics. Analyze each user’s action and attributes then reflect these data to the operation.
Grow - These products are used for the growth of a partner's business. A/B test will Improve application interface. Cloud Messaging will Improve User’s retention.

How can we use it? Simple. We only make a project for the application which wants to use any background task. Then relates them by package name and google-services.json. (Also need to setup some gradle files,but it’s not so difficult to understand)


We don’t have much time to introduce all of them. So I picked up some important products from ‘Develop’ and ‘Grow’ . These products are referred to in the Udacity course.

Database - As you know Database is a type of storage that is designed for analysis and speedy fetch. Firebase provides two different databases. Real Time Database/Fire Store.
And both of them are characterized by the word ‘Realtime’. In this type of database, when there are any updates on stored data, the database itself notify the application. The system is used by the famous online tour company `Skyscanner`. FireStore is a new product. Google recommends using this for a Real Time Database.

Authentication - We often need to construct authentication systems everytime make applications/web systems. That is just a typical task, But we should implement them very carefully because of security,  And we need to chain a lot of different systems for it 
. Firebase Authentication will solve all of the issues related to authentication. It provides not only the backend system, but also it includes User Interfaces. The component supports 
Email registration
Telephony(SMS) authentication
SNS authentication(Google/Facebook/Github/Twitter)

Cloud Messaging(FCM) -  We can send notification messages by using Firebase Cloud Messaging. Currently FCM is the official way to send notification messages over the network. The system is very simple because the system broadcasts a tiny message to the user. It is mainly used for notifying user/retention user activities. But nowadays, the situation is changing, os 

Cloud Functions - Cloud functions provides the environment for running the code written by node.js (you can use js/typescript) format. These codes could be fired by triggers. Like the timing database written/read/Server Request has occurred. 
These functions could glue each component and Google Cloud, and also it is used for some complex task like task queue.  

Refer Udacity Course
Firebase in a weekend
Firebase analytics
