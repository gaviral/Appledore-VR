using UnityEngine;
using System.Collections;
using UnityEngine.VR;
using System.IO;
using System.Collections.Generic;
using System.Text.RegularExpressions;

public class ObjectSpawner : MonoBehaviour
{
   public List<string> gameObjectsNamesList;
   public List<GameObject> gameObjectsList;    //populate list inside of Inspector
   public bool placeMnemonicMode;
   public Camera cam;

   //private Dictionary<int, GameObject> mnemonicDict;
   private Dictionary<string, GameObject> mnemonicDict;
   private string menuSelectedTypeName;
   private List<UniqueGameObject> spawnedObjects;
   private int uidTracker = 0;
   bool wasTouching = false;

   public class UniqueGameObject
   {
      private GameObject gameObject;
      public int uid;
      public string typeName;
      public Vector3 position;
      public Vector3 rotation;

      public UniqueGameObject(GameObject gameObject, int uid, string typeName)
      {
         this.gameObject = gameObject;
         this.uid = uid;
         this.typeName = typeName;
         this.position = this.gameObject.transform.position;
         this.rotation = this.gameObject.transform.rotation.eulerAngles;
      }
   }

    void Start(){
      placeMnemonicMode = true;
      mnemonicDict = new Dictionary<string, GameObject>();
      spawnedObjects = new List<UniqueGameObject>();
      //cam = GetComponent< Camera > ();
      GameObject[] objectsList = gameObjectsList.ToArray();
      string[] names = gameObjectsNamesList.ToArray();

      for(int i = 0; i < objectsList.Length; i++)
      {
         if (!mnemonicDict.ContainsKey(names[i])) {
            mnemonicDict.Add(names[i], objectsList[i]);
         } else {
            Debug.Log("Cannot add prefab to GameObject dictionary - key already exists");
            // can modify to add number to make name unique
         }
      }

      searchMnemonics(" sPiDer/  ");
/*
      int i = 0;
      foreach (GameObject cur in gameObjectsList)
      {
         mnemonicDict.Add(i, cur);
         i++;
      }*/
   }

   public List<string> searchMnemonics(string search)
   {
      Debug.Log("Search Word: " + search);
      search = search.Trim();
      Regex rgx = new Regex("[^a-zA-Z0-9 ]");
      search = rgx.Replace(search, "");
      search = search.ToLower();

      Debug.Log("Search Word After removing whitespaces: " + search);
      List<string> list = new List<string>();

      foreach (string key in mnemonicDict.Keys)
      {
         string temp = key.ToLower();
         if (temp.Contains(search))
         {
            Debug.Log("Key added: " + key);
            list.Add(key);
         }
      }
      Debug.Log("List size: " + list.Count);
      return list;
   }
   
    public Vector3 getMnemonicPosition()
    {
      Vector3 mnemonicPositionVector;

      Ray ray = cam.ViewportPointToRay(new Vector3(0.5f, 0.5f, 0f));

        RaycastHit hit;

        if (Physics.Raycast(ray, out hit)) {
            Debug.DrawRay(ray.origin, ray.direction * 100, Color.red);
            print("I'm looking at " + hit.transform.name);
            mnemonicPositionVector = hit.point;
        }
        else
        {
            mnemonicPositionVector = new Vector3(-55.481f, 15.91f, -97.91f);
        }
        return mnemonicPositionVector;
    }

   public UniqueGameObject placeMnemonic(string typeName)
   {
      Vector3 forward = InputTracking.GetLocalRotation(VRNode.CenterEye) * cam.transform.forward;
      Vector3 spawnPos = cam.transform.position + forward * 2;
      UniqueGameObject uniqueSpawn = null;
      GameObject mnemonic = new GameObject();
      bool result = mnemonicDict.TryGetValue(typeName, out mnemonic);
      if (!result)
      {
         Debug.Log("Couldn't find mnemonic object of typename: " + typeName);
      }
      else
      {
         GameObject spawn = GameObject.Instantiate(mnemonic, spawnPos, Quaternion.identity);
         uniqueSpawn = new UniqueGameObject(spawn, ++uidTracker, typeName);
         spawnedObjects.Add(uniqueSpawn);
         FirebaseHandler database = GetComponent<FirebaseHandler>();
         database.writeUniqueGameObject(uniqueSpawn);
      }
      return uniqueSpawn;
   }

   public void loadSavedGame()
   {
      FirebaseHandler database = GetComponent<FirebaseHandler>();
      database.loadDatabaseUniqueGameObjects();
   }

   public UniqueGameObject loadMnemonic(string typeName, int uid, Vector3 position, Vector3 rotation)
   {
      UniqueGameObject uniqueSpawn = null;
      GameObject mnemonic = new GameObject();
      bool result = mnemonicDict.TryGetValue(typeName, out mnemonic);
      if (!result)
      {
         Debug.Log("Couldn't find mnemonic object of typeid: " + typeName);
      }
      else
      {
         GameObject spawn = GameObject.Instantiate(mnemonic, position, Quaternion.Euler(rotation));
         uniqueSpawn = new UniqueGameObject(spawn, uid, typeName);
         spawnedObjects.Add(uniqueSpawn);
         if (uid > uidTracker)
         {
            uidTracker = uid;
         }
      }
      return uniqueSpawn;
   }

   //example code to connect button to placing mnemonic
   public void placeMaidOnClick()
   {
      //set global variable
      placeMnemonic("spider");
   }

   //Code to connect button to placing mnemonic
   public void placeSpiderOnClick()
   {
      //set global variable
      placeMnemonic("maid");
   }

   public void Update(){
        if (Input.touchCount > 0)
        {
            if (!wasTouching)
            {
                Debug.Log("Touched");
                placeMnemonic(menuSelectedTypeName);
                wasTouching = true;
            }
        }
        else
        {
            wasTouching = false;
        }

        if (Input.GetKeyDown(KeyCode.Space))
        {

            if (placeMnemonicMode)
            {
                placeMnemonic(menuSelectedTypeName);
            }
            //placeMnemonicMode = false; //todo: (hardcoded)
        }
    }
}
/*
 if (hit.rigidbody != null)
        {
            hit.rigidbody.AddForceAtPosition(ray.direction * pokeForce, hit.point);
        }
     */

//    Debug.Log("here: " + x.ToString() + y.ToString() + z.ToString());
//todo: spawn
//todo: write H He shit in the mnemonic
//todo: add rotate
