# CHEAT_TALK
A fake bluetooth chatting android application. Implementing **Single Activity** with following design principle:
* UI Components (Fragments) are only responsible for showing UI based on the data it receive.
* All the actions related to navigation and accessing ViewModel function calling from fragments should be emitted to Main Activity.
* Assign ViewState to each screen view that will appear in application. Using LiveData and Observer to update UI when ViewState changed.
---
## Architecture
![Architecture JPEG](https://i.imgur.com/N9u5sGy.jpg)

* Main Activity : 
    * Handling all the actions, such as database manipulation, networking etc.
    * Create a Navigation Observer to watch ViewState enum class. Make fragment transition or navigation when state updated.
    * Handling service callback from Handler work-queue.
* ViewModel :
    * Store state-related data, so Main Activity can re-rendering the screen when configuration changing happend.
    * Launch all the asynchronous task inside viewmodelScope, so no thread-leaking happend when application terminate.
* Fragments :
    * Emit all the actions trigger by itself to Main Activity, such as a button click to insert data into database. 
* Event Listener :
    * A interface between Main Activity and Fragments. Each fragments should have its own Event Listener to emit action to Main Activity.
* Bound Service (not necessary) :
    * Handling long running task in the background. Launch all the asynchronous task inside its own CoroutineScope.
    * When it has to notify Main Activity to update UI, using android.os.Handler class.  

---
## Advantage
* Make Fragments simple and reusable.
* Easy to handle alternative layout rendering.
* Seperating UI into independent pieces makes cooperation easier.

