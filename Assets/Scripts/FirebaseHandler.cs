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
   private string logText = "";
   const int kMaxLogSize = 16382;
   DependencyStatus dependencyStatus = DependencyStatus.UnavailableOther;

   // When the app starts, check to make sure that we have
   // the required dependencies to use Firebase, and if not,
   // add them if possible.
   void Start()
   {
     // ObjectSpawner spawner = GetComponent<ObjectSpawner>();

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
      loadDatabaseUniqueGameObjectsOnce();
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

      ObjectSpawner spawner = GetComponent<ObjectSpawner>();
      DatabaseReference reference = FirebaseDatabase.DefaultInstance.GetReference("Users").Child(spawner.palaceUserID).Child("palace");
      reference.ChildAdded += HandleChildAdded;
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

   public void writeUniqueGameObject(ObjectSpawner.UniqueGameObject gameObject)
   {
      string json = JsonUtility.ToJson(gameObject);

      DatabaseReference reference = FirebaseDatabase.DefaultInstance.RootReference;
      reference.Child("Users").Child(gameObject.palaceUserID).Child("palace").Child(gameObject.uid.ToString()).SetRawJsonValueAsync(json);
   }

   public void deleteUniqueGameObject(ObjectSpawner.UniqueGameObject gameObject)
   {
      DatabaseReference reference = FirebaseDatabase.DefaultInstance.RootReference;
      reference.Child("Users").Child(gameObject.palaceUserID).Child("palace").Child(gameObject.uid.ToString());
      reference.RemoveValueAsync();
   }

   private void loadSnapshotUniqueGameObjects(DataSnapshot childSnapshot)
   {
      ObjectSpawner spawner = GetComponent<ObjectSpawner>();

      if (childSnapshot != null)
      {
         string typeName = childSnapshot.Child("typeName").Value.ToString();
         string uid = childSnapshot.Child("uid").Value.ToString();

         string temp = childSnapshot.Child("position").Child("x").Value.ToString();
         float x = float.Parse(temp);
         temp = childSnapshot.Child("position").Child("y").Value.ToString();
         float y = float.Parse(temp);
         temp = childSnapshot.Child("position").Child("z").Value.ToString();
         float z = float.Parse(temp);
         Debug.Log("GETTING FIREBASE VALUES: typeName: " + typeName + " uid: " + uid + " position x: " + x + " y: " + y + " z: " + z);
         Vector3 position = new Vector3(x, y, z);

         temp = childSnapshot.Child("rotation").Child("x").Value.ToString();
         x = float.Parse(temp);
         temp = childSnapshot.Child("rotation").Child("y").Value.ToString();
         y = float.Parse(temp);
         temp = childSnapshot.Child("rotation").Child("z").Value.ToString();
         z = float.Parse(temp);
         Debug.Log("GETTING FIREBASE VALUES position:" + " x: " + x + " y: " + y + " z: " + z);

         Vector3 rotation = new Vector3(x, y, z);
         spawner.loadMnemonic(typeName, uid, position, rotation);
         Debug.Log(spawner.spawnedObjects.Count.ToString());
      }
   }

   void HandleChildAdded(object sender, ChildChangedEventArgs args)
   {
      Debug.Log("CALLED HANDLE CHILD ADDED FUNCTION");
      //only if someone else adds object
      if (args.DatabaseError != null)
      {
         Debug.LogError(args.DatabaseError.Message);
         return;
      }
      // Do something with the data in args.Snapshot
      if (args != null)
      {
         loadSnapshotUniqueGameObjects(args.Snapshot);
      }
   }

   public void loadDatabaseUniqueGameObjectsOnce()
   {
      ObjectSpawner spawner = GetComponent<ObjectSpawner>();
      //.Child("palace")

      FirebaseDatabase.DefaultInstance.GetReference("Users").Child(spawner.palaceUserID).Child("palace").GetValueAsync().ContinueWith(task =>
      {
         if (task.IsFaulted)
         {
            Debug.Log("Fault getting game state from Firebase");
         }
         else if (task.IsCompleted)
         {
            DataSnapshot snapshot = task.Result;
            if (snapshot != null && snapshot.ChildrenCount > 0)
            {
               Debug.Log("SNAPSHOT" + snapshot.GetRawJsonValue().ToString());
               foreach (DataSnapshot childSnapshot in snapshot.Children)
               {
                  loadSnapshotUniqueGameObjects(childSnapshot);
               }
            }
         }
      });
   }
}


        
