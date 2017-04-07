using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class displayPrefabs : MonoBehaviour {
    Object object1, object2, object3, object4, object5;
    GameObject prefab1, prefab2, prefab3, prefab4, prefab5;
    GameObject canvas;
    // Use this for initialization
    void Start() {
        testing();
        //spawnAnimals();
        hideAnimals();
    }

    // Update is called once per frame
    void Update() {

    }
    public void testing() {
        object1 = Resources.Load("Menu/Animals/Elephant");
        //GameObject instance = Instantiate(object1, GameObject.Find(""));
    }
    public void checkAnimalsButtonStatus() {
        //Debug.Log("checkStatus");
        if (GameObject.FindGameObjectWithTag("FirstCategoryCanvas").GetComponent<Toggle>().isOn) {
            showAnimals();
        } else {
            hideAnimals();
        }

    }

    public void hideAnimals() {
        Debug.Log("hideAnimals");
        int i = 5;
        for (i = 0; i < 5; i++) {
            SkinnedMeshRenderer renderer = this.gameObject.transform.GetChild(i).GetChild(0).GetChild(0).GetChild(1).GetComponent<SkinnedMeshRenderer>();
            renderer.enabled = false;
            //prefab1.renderer = false;
        }

    }
    public void showAnimals() {
        Debug.Log("showAnimals");
        int i = 5;
        for (i = 0; i < 5; i++) {
            SkinnedMeshRenderer renderer = this.gameObject.transform.GetChild(i).GetChild(0).GetChild(0).GetChild(1).GetComponent<SkinnedMeshRenderer>();
            renderer.enabled = true;
            //prefab1.renderer = false;
        }

    }
    public void spawnAnimals() {
        canvas = GameObject.Find("AnimalsCanvas");
        object1 = Resources.Load("Menu/Animals/Elephant");
        object2 = Resources.Load("Menu/Animals/Gorilla_Howl");
        object3 = Resources.Load("Menu/Animals/SPIDER");
        object4 = Resources.Load("Menu/Animals/Butterfly");
        object5 = Resources.Load("Menu/Animals/Allosaurus_03_Controller"); 
         //MenuCategory category1 = new MenuCategory();
         //GameObject prefab1;
         //Instantiate(Resources.Load("Menu/" + category1.name + "/Elephant")) as GameObject;
         //GameObject instance = Instantiate(Resources.Load("Menu/Animals/Elephant")) as GameObject;
         //Instantiate(, cam.transform);
         prefab1 = Instantiate(object1, canvas.transform.position,
            Quaternion.Euler(new Vector3(0, 223, 0)), canvas.transform) as GameObject;
        prefab1.transform.localScale = new Vector3(598, 598, 598);
        prefab1.transform.localPosition = new Vector3(-195, -40, -32);

        prefab2 = Instantiate(object2, canvas.transform.position,
            Quaternion.Euler(new Vector3(0, 90, 0)), canvas.transform) as GameObject;
        prefab2.transform.localScale = new Vector3(34, 34, 34);
        prefab2.transform.localPosition = new Vector3(-57, -62, -62);

        prefab3 = Instantiate(object3, canvas.transform.position,
            Quaternion.Euler(new Vector3(10, 227, 2)), canvas.transform) as GameObject;
        prefab3.transform.localScale = new Vector3(14, 14, 14);
        prefab3.transform.localPosition = new Vector3(7, -74, -40);

        prefab4 = Instantiate(object4, canvas.transform.position,
            Quaternion.Euler(new Vector3(0, 176, 0)), canvas.transform) as GameObject;
        prefab4.transform.localScale = new Vector3(34, 34, 34);
        prefab4.transform.localPosition = new Vector3(76, -62, -62);

        prefab5 = Instantiate(object3, canvas.transform.position,
            Quaternion.Euler(new Vector3(0, 100, 0)), canvas.transform) as GameObject;
        prefab5.transform.localScale = new Vector3(34, 34, 34);
        prefab5.transform.localPosition = new Vector3(190, -79, -78);
    }
}


