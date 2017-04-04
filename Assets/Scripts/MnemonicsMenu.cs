using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;
using UnityEngine.UI;

public class MnemonicsMenu : MonoBehaviour {

    private int curMnemonicNum;
    private int curCategoryPageNum;
    private int numOfCategories;
    private int numOfCategoriesBeingDisplayed;
    private string curCategoryName;
    private DirectoryInfo[] categoryDirectory;
    private DirectoryInfo menuPath;

    // Use this for initialization
    void Start () {
        //variable initialization
        curMnemonicNum = -1;
        curCategoryPageNum = 0; // 0 1 2 3 and so on

        setMenuDirectoryPath();
        setNumOfCategoriesBeingDisplayed();
        getCategoriesInfo();
        getFilesInfo();
        updateCategoryCubeNames();
    }

    private void getFilesInfo() {
        //FileInfo[] fileInfo = levelDirectoryPath.GetFiles("*.*", SearchOption.AllDirectories);
        int categoryCubeNum = 0;
        for (categoryCubeNum = 0; categoryCubeNum < numOfCategoriesBeingDisplayed; categoryCubeNum++) {
            Debug.Log("path: " + categoryDirectory[getCategoryNumOffset() + categoryCubeNum]);
        } 
        
//        FileInfo[] mnemonicFile = ;
    }

    // Update is called once per frame
    void Update () {
        
    }

    public void categoryCubeClicked(int categoryCubeNum) {
        //Object object1 = Resources.Load("Menu/Animals/Elephant");
        Debug.Log("Cube #" + categoryCubeNum.ToString() +" was clicked");
        //clearDisplayArea();
        curCategoryName = categoryDirectory[getCategoryNumOffset() + categoryCubeNum].Name;
        Debug.Log(curCategoryName);
        setDisplayAreaTitle();
        displayMnemonic();
    }

    private void displayMnemonic() {
        throw new NotImplementedException();
    }

    private void setDisplayAreaTitle() {
        Debug.Log(gameObject.GetComponentInChildren<Text>().text = curCategoryName);
    }

    public void getCategoriesInfo() {
        categoryDirectory = menuPath.GetDirectories();
        numOfCategories = categoryDirectory.Length;
        Debug.Log("Number of Category Directories: " + numOfCategories);
    }

    public void setMenuDirectoryPath() {
        menuPath = new DirectoryInfo("Assets/Resources/Menu/");
    }

    public void clearDisplayArea() {
        throw new NotImplementedException();
    }

    public int getCategoryNumOffset() {
        return numOfCategoriesBeingDisplayed * curCategoryPageNum;
    }

    public void setNumOfCategoriesBeingDisplayed() {
        numOfCategoriesBeingDisplayed = 5;
    }

    public void nextCategoryPage() {
        ++curCategoryPageNum;
        updateCategoryCubeNames();
    }

    public void previousCategoryPage() {
        --curCategoryPageNum;
        updateCategoryCubeNames();
    }

    private void updateCategoryCubeNames() {
        GameObject.Find("CategoryCube0").GetComponentInChildren<Text>().text = categoryDirectory[getCategoryNumOffset() + 0].Name;
        GameObject.Find("CategoryCube1").GetComponentInChildren<Text>().text = categoryDirectory[getCategoryNumOffset() + 1].Name;
        GameObject.Find("CategoryCube2").GetComponentInChildren<Text>().text = categoryDirectory[getCategoryNumOffset() + 2].Name;
        GameObject.Find("CategoryCube3").GetComponentInChildren<Text>().text = categoryDirectory[getCategoryNumOffset() + 3].Name;
        GameObject.Find("CategoryCube4").GetComponentInChildren<Text>().text = categoryDirectory[getCategoryNumOffset() + 4].Name;
    }
}

/*
//Debug.Log("showCatergory");
DirectoryInfo levelDirectoryPath = new DirectoryInfo("Assets/Resources/Menu/");
DirectoryInfo[] categoryDirectories = levelDirectoryPath.GetDirectories();

//   FileInfo[] fileInfo = levelDirectoryPath.GetFiles("*.*", SearchOption.AllDirectories);
int i = 0;
        foreach (DirectoryInfo category in categoryDirectories) {
            //Debug.Log(txt);
            //Debug.Log(category.Name);
            Debug.Log(this.gameObject.transform.GetChild(i).GetComponent<Text>().text = category.Name);
i++;
}*/