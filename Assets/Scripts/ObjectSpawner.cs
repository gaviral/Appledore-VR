using UnityEngine;
using System.Collections;
using UnityEngine.VR;
using System.IO;
using System.Collections.Generic;

public class ObjectSpawner : MonoBehaviour
{
   public List<GameObject> gameObjectsList;    //populate list inside of Inspector
   public bool placeMnemonicMode;
   public Camera cam;

   private Dictionary<int, GameObject> mnemonicDict; 
   private int menuSelectedID;
   private List<UniqueGameObject> spawnedObjects;
   private int uidTracker = 0;
   bool wasTouching = false;

   public class UniqueGameObject
   {
      private GameObject gameObject;
      public int uid;
      public int typeId;
      public Vector3 position;
      public Quaternion rotation;

      public UniqueGameObject(GameObject gameObject, int uid, int typeId)
      {
         this.gameObject = gameObject;
         this.uid = uid;
         this.typeId = typeId;
         this.position = this.gameObject.transform.position;
         this.rotation = this.gameObject.transform.rotation;
      }
   }

    void Start(){
      placeMnemonicMode = true;
      mnemonicDict = new Dictionary<int, GameObject>();
      spawnedObjects = new List<UniqueGameObject>();
      //cam = GetComponent< Camera > ();
      int i = 0;
      foreach (GameObject cur in gameObjectsList)
      {
         mnemonicDict.Add(i, cur);
         i++;
      }
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

   public UniqueGameObject placeMnemonic(int typeId)
   {
      Vector3 forward = InputTracking.GetLocalRotation(VRNode.CenterEye) * cam.transform.forward;
      Vector3 spawnPos = cam.transform.position + forward * 2;
      UniqueGameObject uniqueSpawn = null;
      GameObject mnemonic = new GameObject();
      bool result = mnemonicDict.TryGetValue(typeId, out mnemonic);
      if (!result)
      {
         Debug.Log("Couldn't find mnemonic object of typeid: " + typeId);
      }
      else
      {
         GameObject spawn = GameObject.Instantiate(mnemonic, spawnPos, Quaternion.identity);
         uniqueSpawn = new UniqueGameObject(spawn, ++uidTracker, typeId);
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

   public UniqueGameObject loadMnemonic(int typeId, int uid, Vector3 position, Quaternion rotation)
   {
      UniqueGameObject uniqueSpawn = null;
      GameObject mnemonic = new GameObject();
      bool result = mnemonicDict.TryGetValue(typeId, out mnemonic);
      if (!result)
      {
         Debug.Log("Couldn't find mnemonic object of typeid: " + typeId);
      }
      else
      {
         GameObject spawn = GameObject.Instantiate(mnemonic, position, rotation);
         uniqueSpawn = new UniqueGameObject(spawn, uid, typeId);
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
      placeMnemonic(0);
   }

   //Code to connect button to placing mnemonic
   public void placeSpiderOnClick()
   {
      //set global variable
      placeMnemonic(1);
   }

   public void Update(){
        if (Input.touchCount > 0)
        {
            if (!wasTouching)
            {
                Debug.Log("Touched");
                placeMnemonic(menuSelectedID);
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
                placeMnemonic(menuSelectedID);
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
