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
    private FileInfo[] prefabFileInfo0;
    private FileInfo[] prefabFileInfo1;
    private FileInfo[] prefabFileInfo2;
    private FileInfo[] prefabFileInfo3;
    private FileInfo[] prefabFileInfo4;
    private UnityEngine.Object object1;
    private int curCategoryNum;
    GameObject currentDisplayedPrefab;

    // Use this for initialization
    void Start () {
        //variable initialization
        curMnemonicNum = 0;
        curCategoryPageNum = 0; // 0 1 2 3 and so on

        setMenuDirectoryPath();
        setNumOfCategoriesBeingDisplayed();
        getCategoriesInfo();
        getFilesInfo();
        updateCategoryCubeNames();
        
    }

    private void getFilesInfo() {
        //FileInfo[] fileInfo = levelDirectoryPath.GetFiles("*.*", SearchOption.AllDirectories);
        //  int categoryCubeNum;
        //for (categoryCubeNum = 0; categoryCubeNum < numOfCategoriesBeingDisplayed; categoryCubeNum++) {
        //Debug.Log("path: " + categoryDirectory[getCategoryNumOffset() + categoryCubeNum]);

        int numCategories = getNumOfCategoriesInCurrentPage();
        if (numCategories>0) {
            Debug.Log("numCategories>0");
            prefabFileInfo0 = categoryDirectory[getCategoryNumOffset() + 0].GetFiles("*.prefab");
            //Debug.Log("getFiles:" + categoryDirectory[getCategoryNumOffset() + 0].GetFiles("*.prefab"));
        }
        if (numCategories>1) {
          //  Debug.Log("getFilesInfo");
            prefabFileInfo1 = categoryDirectory[getCategoryNumOffset() + 1].GetFiles("*.prefab");
        }
        if (numCategories > 2) {
           // Debug.Log("getFilesInfo");
            prefabFileInfo2 = categoryDirectory[getCategoryNumOffset() + 2].GetFiles("*.prefab");
        }
        if (numCategories > 3) {
          //  Debug.Log("getFilesInfo");
            prefabFileInfo3 = categoryDirectory[getCategoryNumOffset() + 3].GetFiles("*.prefab");
        }
        if (numCategories > 4) {
         //   Debug.Log("getFilesInfo");
            prefabFileInfo4 = categoryDirectory[getCategoryNumOffset() + 4].GetFiles("*.prefab");
        }

        //}
        
         foreach (FileInfo file in prefabFileInfo0) {
            Debug.Log("file: " + file.Name);
            
            //fileInfo[categoryCubeNum] = categoryDirectory[getCategoryNumOffset() + categoryCubeNum].GetFiles("*.prefab");
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
        curCategoryNum = categoryCubeNum;
        curCategoryName = categoryDirectory[getCategoryNumOffset() + categoryCubeNum].Name;
        Debug.Log(curCategoryName);
        setDisplayAreaTitle();
        displayMnemonic();
    }

    private void displayMnemonic() {
        FileInfo[] correctPrefab = getCorrectPrefab();
        // Debug.Log(correctPrefab[curMnemonicNum].ToString());
        Debug.Log(curMnemonicNum);
        Debug.Log("displayMnemonics curMnemonicNum: " + "/Menu/" + curCategoryName + "/" + Path.GetFileNameWithoutExtension(correctPrefab[curMnemonicNum].Name) );
        object1 = Resources.Load("Menu/" + curCategoryName + "/" + Path.GetFileNameWithoutExtension(correctPrefab[curMnemonicNum].Name));
        Debug.Log(object1);
        currentDisplayedPrefab = Instantiate(object1, this.gameObject.transform.GetChild(0).transform) as GameObject;
        
    }

    private FileInfo[] getCorrectPrefab() {
        switch (curCategoryNum) {
            case 0: return prefabFileInfo0;
            case 1: return prefabFileInfo1;
            case 2: return prefabFileInfo2;
            case 3: return prefabFileInfo3;
            default: return prefabFileInfo0;
        }
    }

    private void setDisplayAreaTitle() {
        gameObject.GetComponentInChildren<Text>().text = curCategoryName;
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
        destroyCurrentlyDisplayedPrefab();
    }

    private void destroyCurrentlyDisplayedPrefab() {
        Destroy(currentDisplayedPrefab);
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
        decrementCurMnemonicNum();
        updateCategoryCubeNames();
    }

    private void decrementCurMnemonicNum() {
        curMnemonicNum--;
        if (curMnemonicNum == -1)
            curMnemonicNum++;
    }

    public void displayCanvasRightButtonClicked() {
        Debug.Log("insideDisplayCanvasRightButtonClicked()");
        clearDisplayArea();
        incrementCurMnemonicNum();
        
        displayMnemonic();
    }

    private void incrementCurMnemonicNum() {
        curMnemonicNum++;
        if (curMnemonicNum == prefabFileInfo0.Length)
            curMnemonicNum--;
    }

    public void displayCanvasLeftButtonClicked() {
        Debug.Log("insideDisplayCanvasLeftButtonClicked()");
        clearDisplayArea();
        curMnemonicNum--;
        displayMnemonic();
    }

    private void updateCategoryCubeNames() {
        int numCategories = getNumOfCategoriesInCurrentPage();
        if (numCategories>4)
            GameObject.Find("CategoryCube4").GetComponentInChildren<Text>().text = categoryDirectory[getCategoryNumOffset() + 4].Name;
        if (numCategories > 3)
            GameObject.Find("CategoryCube3").GetComponentInChildren<Text>().text = categoryDirectory[getCategoryNumOffset() + 3].Name;
        if (numCategories > 2)
            GameObject.Find("CategoryCube2").GetComponentInChildren<Text>().text = categoryDirectory[getCategoryNumOffset() + 2].Name;
        if (numCategories > 1)
            GameObject.Find("CategoryCube1").GetComponentInChildren<Text>().text = categoryDirectory[getCategoryNumOffset() + 1].Name;
        if (numCategories > 0)
            GameObject.Find("CategoryCube0").GetComponentInChildren<Text>().text = categoryDirectory[getCategoryNumOffset() + 0].Name;
    }

    private int getNumOfCategoriesInCurrentPage() {
        return 4; //HARDCODED
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