using UnityEngine;
using System.Collections;
using UnityEngine.VR;
using System.IO;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using UnityEngine.EventSystems;

public class ObjectSpawner : MonoBehaviour {

    [HideInInspector]
    public AndroidJavaClass mainClass;

    [HideInInspector]
    public string userID;

    [HideInInspector]
    public string palaceUserID;

    //public List<string> gameObjectsNamesList;    //populate lits inside of Inspector
    //public List<GameObject> gameObjectsList;    //populate list inside of Inspector
    public bool placeMnemonicMode;

    public Camera cam;

    // public Dictionary<string, GameObject> mnemonicDict;
    public Dictionary<string, Object> mnemonicDict;

    private string menuSelectedTypeName;
    private HashSet<string> uidSet;
    public List<UniqueGameObject> spawnedObjects;
    private int uidTracker = 0;
    private bool wasTouching = false;

    private DirectoryInfo[] categoryDirectories;
    private DirectoryInfo menuPath;

    public class UniqueGameObject {
        public GameObject gameObject;
        public string uid;
        public string palaceUserID;
        public string typeName;
        public Vector3 position;
        public Vector3 rotation;

        public UniqueGameObject(GameObject gameObject, string uid, string palaceUserID, string typeName) {
            this.gameObject = gameObject;
            this.uid = uid;
            this.palaceUserID = palaceUserID;
            this.typeName = typeName;
            this.position = this.gameObject.transform.position;
            this.rotation = this.gameObject.transform.rotation.eulerAngles;
        }
    }

    private void Awake() {
       // mainClass = new AndroidJavaClass("com.vroneinc.vrone.PalaceSelectionActivity");
        //userID = mainClass.CallStatic<string>("getCurUserId");
        //palaceUserID = mainClass.CallStatic<string>("getCurPalaceUserId");
        userID = "OfGU9MONj7PiIinOJWUVbknfIpE2";
        palaceUserID = "jnPhEjWfVEMI68T0tITXnZnerc92";

        placeMnemonicMode = true;
        //mnemonicDict = new Dictionary<string, GameObject>();
        mnemonicDict = new Dictionary<string, Object>();
        spawnedObjects = new List<UniqueGameObject>();
        uidSet = new HashSet<string>();

        initMnemonicDictFromResources();
        /*
        //code for inspector dictionary loading
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
        }*/
    }

    private void initMnemonicDictFromResources() {
        //menuPath = new DirectoryInfo("Assets/Resources/Menu/");
        //categoryDirectories = menuPath.GetDirectories();

        Object[] allObjects = Resources.LoadAll("Menu");
        foreach (Object obj in allObjects) {
            string keyName = obj.name;
            if (!mnemonicDict.ContainsKey(keyName)) {
                mnemonicDict.Add(keyName, obj);
            } else {
                Debug.Log("Cannot add prefab to GameObject dictionary - key already exists");
                // can modify to add number to make name unique
            }
        }
        /*
      for (int i = 0; i < categoryDirectories.Length; i++)
      {
         FileInfo[] fileInfoArray = categoryDirectories[i].GetFiles("*.prefab");
         int fileInfoArraySize = fileInfoArray.Length;
         for (int j = 0; j < fileInfoArraySize; j++)
         {
            string keyName = Path.GetFileNameWithoutExtension(fileInfoArray[j].Name);
            Object obj = Resources.Load("Menu/" + categoryDirectories[i].Name + "/" + keyName);
            if (!mnemonicDict.ContainsKey(keyName))
            {
               mnemonicDict.Add(keyName, obj);
            }
            else
            {
               Debug.Log("Cannot add prefab to GameObject dictionary - key already exists");
               // can modify to add number to make name unique
            }
         }
      }*/
    }

    public List<string> searchMnemonics(string search) {
        Debug.Log("Search Word: " + search);
        search = search.Trim();
        Regex rgx = new Regex("[^a-zA-Z0-9 ]");
        search = rgx.Replace(search, "");
        search = search.ToLower();

        Debug.Log("Search Word After removing whitespaces: " + search);
        List<string> list = new List<string>();

        foreach (string key in mnemonicDict.Keys) {
            string temp = key.ToLower();
            if (temp.Contains(search)) {
                Debug.Log("Key added: " + key);
                list.Add(key);
            }
        }
        Debug.Log("List size: " + list.Count);
        return list;
    }

    public Vector3 getMnemonicPosition() {
        Vector3 mnemonicPositionVector;

        Ray ray = cam.ViewportPointToRay(new Vector3(0.5f, 0.5f, 0f));

        RaycastHit hit;

        if (Physics.Raycast(ray, out hit)) {
            Debug.DrawRay(ray.origin, ray.direction * 100, Color.red);
            print("I'm looking at " + hit.transform.name);
            mnemonicPositionVector = hit.point;
        } else {
            mnemonicPositionVector = new Vector3(-55.481f, 15.91f, -97.91f);
        }
        return mnemonicPositionVector;
    }

    public void addMnemonicFromMenu(string typeName, GameObject spawn) {
        uidTracker++;
        string uid = userID + " " + uidTracker.ToString();
        if (!uidSet.Contains(uid)) {
            UniqueGameObject uniqueSpawn = new UniqueGameObject(spawn, uid, palaceUserID, typeName);
            spawnedObjects.Add(uniqueSpawn);
            uidSet.Add(uid);
            FirebaseHandler database = GetComponent<FirebaseHandler>();
            database.writeUniqueGameObject(uniqueSpawn);
        } else {
            Debug.Log("uidTracker broken - uid already exists");
        }
    }

    public UniqueGameObject placeMnemonic(string typeName) {
        Vector3 forward = InputTracking.GetLocalRotation(VRNode.CenterEye) * cam.transform.forward;
        Vector3 spawnPos = cam.transform.position + forward * 2;
        UniqueGameObject uniqueSpawn = null;
        //GameObject mnemonic = new GameObject();
        Object mnemonic = new Object();
        bool result = mnemonicDict.TryGetValue(typeName, out mnemonic);
        if (!result) {
            Debug.Log("Couldn't find mnemonic object of typename: " + typeName);
        } else {
            uidTracker++;
            string uid = userID + " " + uidTracker.ToString();
            if (!uidSet.Contains(uid)) {
                GameObject spawn = (GameObject)Instantiate(mnemonic, spawnPos, Quaternion.identity) as GameObject;
                uniqueSpawn = new UniqueGameObject(spawn, uid, palaceUserID, typeName);
                spawnedObjects.Add(uniqueSpawn);
                uidSet.Add(uid);
                FirebaseHandler database = GetComponent<FirebaseHandler>();
                database.writeUniqueGameObject(uniqueSpawn);
            } else {
                Debug.Log("uidTracker broken - uid already exists");
            }
        }
        return uniqueSpawn;
    }

    public UniqueGameObject loadMnemonic(string typeName, string uid, Vector3 position, Vector3 rotation) {
        UniqueGameObject uniqueSpawn = null;
        //GameObject mnemonic = new GameObject();
        Object mnemonic = new Object();
        bool result = mnemonicDict.TryGetValue(typeName, out mnemonic);
        if (!result) {
            Debug.Log("Couldn't find mnemonic object of typeid: " + typeName);
        } else {
            if (!uidSet.Contains(uid)) {
                  GameObject spawn = (GameObject)Instantiate(mnemonic, position, Quaternion.Euler(rotation)) as GameObject;

                  spawn.AddComponent<ObjectDestroyer>();

                  uniqueSpawn = new UniqueGameObject(spawn, uid, palaceUserID, typeName);
                spawnedObjects.Add(uniqueSpawn);

                //for when objects are loaded at start of game
                string[] temp = uid.Split(' ');
                if (temp[0].Equals(userID))   //uid tracker is local to each user, not all users
                {
                    int uidint = int.Parse(temp[1]);
                    if (uidint > uidTracker) {
                        uidTracker = uidint;
                    }
                }
            }
        }
        return uniqueSpawn;
    }

    /*
    public UniqueGameObject removeMnemonic(string typeName)
    {
          GameObject spawn = GameObject.Instantiate(mnemonic, spawnPos, Quaternion.identity);
          uidTracker++;
          string uid = userID + " " + uidTracker.ToString();
          if (!uidSet.Contains(uid))
          {
             uniqueSpawn = new UniqueGameObject(spawn, uid, palaceUserID, typeName);
             spawnedObjects.Add(uniqueSpawn);
             uidSet.Add(uid);
             FirebaseHandler database = GetComponent<FirebaseHandler>();
             database.writeUniqueGameObject(uniqueSpawn);
          }
          else
          {
             Debug.Log("uidTracker broken - uid already exists");
          }
       }
       return uniqueSpawn;
    }*/

    private void removeUniqueGameObject(GameObject gameObject) {
        foreach (UniqueGameObject cur in spawnedObjects) {
            if (cur.gameObject == gameObject) {
                uidSet.Remove(cur.uid);
                spawnedObjects.Remove(cur);
                FirebaseHandler database = GetComponent<FirebaseHandler>();
                database.deleteUniqueGameObject(cur);
                break;
            }
        }
    }

    //example code to connect button to placing mnemonic
    public void placeMaidOnClick() {
        //set global variable
        placeMnemonic("Elephant");
    }

    //Code to connect button to placing mnemonic
    public void placeSpiderOnClick() {
        //set global variable
        placeMnemonic("SPIDER");
    }

    public void Update() {
        if (Input.touchCount > 0) {
            if (!wasTouching) {
                Debug.Log("Touched");
                placeMnemonic(menuSelectedTypeName);
                wasTouching = true;
            }
        } else {
            wasTouching = false;
        }

        if (Input.GetKeyDown(KeyCode.Space)) {
            if (placeMnemonicMode) {
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