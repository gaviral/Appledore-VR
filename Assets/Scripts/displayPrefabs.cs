using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class displayPrefabs : MonoBehaviour {

    // Use this for initialization
    void Start() {

        //spawnAnimals();
    }

    // Update is called once per frame
    void Update() {

    }

    public void spawnAnimals() {
        GameObject canvas = GameObject.Find("DisplayCanvas");
        Object object1 = Resources.Load("Menu/Animals/Elephant");
        Object object2 = Resources.Load("Menu/Animals/Gorilla_Howl");
        Object object3 = Resources.Load("Menu/Animals/SPIDER");
        Object object4 = Resources.Load("Menu/Animals/Butterfly");
        Object object5 = Resources.Load("Menu/Animals/Allosaurus_03_Controller"); 
         //MenuCategory category1 = new MenuCategory();
         //GameObject prefab1;
         //Instantiate(Resources.Load("Menu/" + category1.name + "/Elephant")) as GameObject;
         //GameObject instance = Instantiate(Resources.Load("Menu/Animals/Elephant")) as GameObject;
         //Instantiate(, cam.transform);
         GameObject prefab1 = Instantiate(object1, canvas.transform.position,
            Quaternion.Euler(new Vector3(0, 223, 0)), canvas.transform) as GameObject;
        prefab1.transform.localScale = new Vector3(598, 598, 598);
        prefab1.transform.localPosition = new Vector3(-195, -40, -32);

        GameObject prefab2 = Instantiate(object2, canvas.transform.position,
            Quaternion.Euler(new Vector3(0, 90, 0)), canvas.transform) as GameObject;
        prefab2.transform.localScale = new Vector3(34, 34, 34);
        prefab2.transform.localPosition = new Vector3(-57, -62, -62);

        GameObject prefab3 = Instantiate(object3, canvas.transform.position,
            Quaternion.Euler(new Vector3(10, 227, 2)), canvas.transform) as GameObject;
        prefab3.transform.localScale = new Vector3(14, 14, 14);
        prefab3.transform.localPosition = new Vector3(7, -74, -40);

        GameObject prefab4 = Instantiate(object4, canvas.transform.position,
            Quaternion.Euler(new Vector3(0, 176, 0)), canvas.transform) as GameObject;
        prefab4.transform.localScale = new Vector3(34, 34, 34);
        prefab4.transform.localPosition = new Vector3(76, -62, -62);

        GameObject prefab5 = Instantiate(object3, canvas.transform.position,
            Quaternion.Euler(new Vector3(0, 100, 0)), canvas.transform) as GameObject;
        prefab5.transform.localScale = new Vector3(34, 34, 34);
        prefab5.transform.localPosition = new Vector3(190, -79, -78);
    }
}


