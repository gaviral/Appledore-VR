using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;
using UnityEngine.UI;

public class MenuCategory {
    public string name = "Animals";
}
public class menu : MonoBehaviour {

    // Use this for initialization
    void Start() {
        showCategories();
       /* 
        for (i = 0; i < 5; i++) {
            
        }
        */
        //.GetComponent<Text>().text = "123");
    }

    // Update is called once per frame
    void Update() {

    }

    public void showCategories() {
        //Debug.Log("showCatergory");
        DirectoryInfo levelDirectoryPath = new DirectoryInfo("Assets/Resources/Menu/");
        DirectoryInfo[] categoryDirectories = levelDirectoryPath.GetDirectories();

        //   FileInfo[] fileInfo = levelDirectoryPath.GetFiles("*.*", SearchOption.AllDirectories);
        int i=0;
        foreach (DirectoryInfo category in categoryDirectories) {
            //Debug.Log(txt);
            //Debug.Log(category.Name);
            Debug.Log(this.gameObject.transform.GetChild(i).GetComponent<Text>().text = category.Name);
            i++;
        }

    }
}
