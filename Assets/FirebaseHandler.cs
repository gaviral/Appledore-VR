using Firebase;
using Firebase.Database;
using Firebase.Unity.Editor;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Threading.Tasks;
using UnityEngine;
using UnityEngine.UI;

public class FirebaseHandler : MonoBehaviour
{

   // ArrayList items;

   private string logText = "";


   const int kMaxLogSize = 16382;
   DependencyStatus dependencyStatus = DependencyStatus.UnavailableOther;

   // When the app starts, check to make sure that we have
   // the required dependencies to use Firebase, and if not,
   // add them if possible.
   void Start()
   {
      dependencyStatus = FirebaseApp.CheckDependencies();
      if (dependencyStatus != DependencyStatus.Available)
      {
         FirebaseApp.FixDependenciesAsync().ContinueWith(task => {
            dependencyStatus = FirebaseApp.CheckDependencies();
            if (dependencyStatus == DependencyStatus.Available)
            {
               InitializeFirebase();
            }
            else
            {
               Debug.LogError(
                   "Could not resolve all Firebase dependencies: " + dependencyStatus);
            }
         });
      }
      else
      {
         InitializeFirebase();
      }

      writeNewItem("itemidstart2342342", "itemposstart3214234");
   }

   public class Item
   {
      public string id;
      public string pos;

      public Item()
      {
      }

      public Item(string id, string pos)
      {
         this.id = id;
         this.pos = pos;
      }
   }

   // Initialize the Firebase database:
   void InitializeFirebase()
   {
      FirebaseApp app = FirebaseApp.DefaultInstance;
      app.SetEditorDatabaseUrl("https://vr-one-4e3bb.firebaseio.com/");
      if (app.Options.DatabaseUrl != null) app.SetEditorDatabaseUrl(app.Options.DatabaseUrl);

      //items = new ArrayList();
   }

   // Exit if escape (or back, on mobile) is pressed.
   void Update()
   {
      if (Input.GetKeyDown(KeyCode.Escape))
      {
         Application.Quit();
      }
   }

   // Output text to the debug log text field, as well as the console.
   public void DebugLog(string s)
   {
      Debug.Log(s);
      logText += s + "\n";

      while (logText.Length > kMaxLogSize)
      {
         int index = logText.IndexOf("\n");
         logText = logText.Substring(index + 1);
      }
   }

   private void writeNewItem(string itemId, string pos)
   {
      Item item = new Item(itemId, pos);
      string json = JsonUtility.ToJson(item);

      DatabaseReference reference = FirebaseDatabase.DefaultInstance.RootReference;
      reference.Child("users").Child(itemId).SetRawJsonValueAsync(json);
   }

   public void writeNewItemOnClick()
   {
      Item item = new Item("itemidonclick23234", "itemposonclick23423");
      string json = JsonUtility.ToJson(item);

      DatabaseReference reference = FirebaseDatabase.DefaultInstance.RootReference;
      reference.Child("items").Child("itemidonclick23234").SetRawJsonValueAsync(json);
   }
}
