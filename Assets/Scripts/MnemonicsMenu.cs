using System;
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
    private UnityEngine.Object curObject;
    private int curCategoryNum;
    private GameObject currentlyDisplayedMenuGameObject;

    // Use this for initialization
    private void Start() {
        //variable initialization
        curMnemonicNum = 0;
        curCategoryPageNum = 0; // 0 1 2 3 and so on
        SetMenuDirectoryPath();
        SetNumOfCategoriesBeingDisplayed();
        GetCategoriesInfo();
        GetFilesInfo();
        UpdateCategoryCubeNames();
    }

    private void GetFilesInfo() {
        //FileInfo[] fileInfo = levelDirectoryPath.GetFiles("*.*", SearchOption.AllDirectories);
        //  int categoryCubeNum;
        //for (categoryCubeNum = 0; categoryCubeNum < numOfCategoriesBeingDisplayed; categoryCubeNum++) {
        //Debug.Log("path: " + categoryDirectory[getCategoryNumOffset() + categoryCubeNum]);

        int numCategories = GetNumOfCategoriesInCurrentPage();
        if (numCategories > 0) {
            Debug.Log("numCategories>0");
            prefabFileInfo0 = categoryDirectory[GetCategoryNumOffset() + 0].GetFiles("*.prefab");
            //Debug.Log("getFiles:" + categoryDirectory[getCategoryNumOffset() + 0].GetFiles("*.prefab"));
        }
        if (numCategories > 1) {
            //  Debug.Log("getFilesInfo");
            prefabFileInfo1 = categoryDirectory[GetCategoryNumOffset() + 1].GetFiles("*.prefab");
        }
        if (numCategories > 2) {
            // Debug.Log("getFilesInfo");
            prefabFileInfo2 = categoryDirectory[GetCategoryNumOffset() + 2].GetFiles("*.prefab");
        }
        if (numCategories > 3) {
            //  Debug.Log("getFilesInfo");
            prefabFileInfo3 = categoryDirectory[GetCategoryNumOffset() + 3].GetFiles("*.prefab");
        }
        if (numCategories > 4) {
            //   Debug.Log("getFilesInfo");
            prefabFileInfo4 = categoryDirectory[GetCategoryNumOffset() + 4].GetFiles("*.prefab");
        }

        //}

        foreach (FileInfo file in prefabFileInfo0) {
            Debug.Log("file: " + file.Name);

            //fileInfo[categoryCubeNum] = categoryDirectory[getCategoryNumOffset() + categoryCubeNum].GetFiles("*.prefab");
        }

        //        FileInfo[] mnemonicFile = ;
    }

    // Update is called once per frame
    private void Update() {
        if (GameObject.Find("DisplayAreaTitleText").GetComponent<Text>().text.Equals("")) {
            Debug.Log("DisplayAreaTitleText is empty");
            GameObject.Find("MenuSpawnButton").GetComponent<BoxCollider>().enabled = false;
        } else {
            GameObject.Find("MenuSpawnButton").GetComponent<BoxCollider>().enabled = true;
        }
    }

    public void CategoryCubeClicked(int categoryCubeNum) {
        //Object object1 = Resources.Load("Menu/Animals/Elephant");
        Debug.Log("Cube #" + categoryCubeNum.ToString() + " was clicked");
        ClearDisplayArea();
        curCategoryNum = categoryCubeNum;
        curCategoryName = categoryDirectory[GetCategoryNumOffset() + categoryCubeNum].Name;
        Debug.Log(curCategoryName);
        SetDisplayAreaTitle();
        DisplayMnemonic();
    }

    private void DisplayMnemonic() {
        FileInfo[] correctPrefab = GetCorrectPrefab();
        // Debug.Log(correctPrefab[curMnemonicNum].ToString());
        Debug.Log(curMnemonicNum);
        Debug.Log("displayMnemonics curMnemonicNum: " + "/Menu/" + curCategoryName + "/" + Path.GetFileNameWithoutExtension(correctPrefab[curMnemonicNum].Name));
        curObject = Resources.Load("Menu/" + curCategoryName + "/" + Path.GetFileNameWithoutExtension(correctPrefab[curMnemonicNum].Name));
        Debug.Log(curObject);
        currentlyDisplayedMenuGameObject = Instantiate(curObject, this.gameObject.transform.GetChild(0).transform) as GameObject;
    }

    private FileInfo[] GetCorrectPrefab() {
        switch (curCategoryNum) {
            case 0: return prefabFileInfo0;
            case 1: return prefabFileInfo1;
            case 2: return prefabFileInfo2;
            case 3: return prefabFileInfo3;
            default: return prefabFileInfo0;
        }
    }

    private void SetDisplayAreaTitle() {
        gameObject.GetComponentInChildren<Text>().text = curCategoryName;
    }

    public void GetCategoriesInfo() {
        categoryDirectory = menuPath.GetDirectories();
        numOfCategories = categoryDirectory.Length;
        Debug.Log("Number of Category Directories: " + numOfCategories);
    }

    public void SetMenuDirectoryPath() {
        menuPath = new DirectoryInfo("Assets/Resources/Menu/");
    }

    public void ClearDisplayArea() {
        DestroyCurrentlyDisplayedPrefab();
    }

    public void DealWithCubes(string sentBy) {
        if (sentBy.Equals("MenuSpawnButton")) {
            Debug.Log("Sent by Spawn Button");
            GameObject.Find("MenuButton").GetComponent<Toggle>().isOn = false;
        }
        if (GameObject.Find("MenuButton").GetComponent<Toggle>().isOn) {
            GameObject.Find("CategoryCube0").GetComponent<MeshRenderer>().enabled = true;
            GameObject.Find("CategoryCube1").GetComponent<MeshRenderer>().enabled = true;
            GameObject.Find("CategoryCube2").GetComponent<MeshRenderer>().enabled = true;
            GameObject.Find("CategoryCube3").GetComponent<MeshRenderer>().enabled = true;
            GameObject.Find("CategoryCube4").GetComponent<MeshRenderer>().enabled = true;
            GameObject.Find("MenuSpawnButton").GetComponent<MeshRenderer>().enabled = true;
        } else {
            GameObject.Find("CategoryCube0").GetComponent<MeshRenderer>().enabled = false;
            GameObject.Find("CategoryCube1").GetComponent<MeshRenderer>().enabled = false;
            GameObject.Find("CategoryCube2").GetComponent<MeshRenderer>().enabled = false;
            GameObject.Find("CategoryCube3").GetComponent<MeshRenderer>().enabled = false;
            GameObject.Find("CategoryCube4").GetComponent<MeshRenderer>().enabled = false;
            GameObject.Find("MenuSpawnButton").GetComponent<MeshRenderer>().enabled = false;
        }
    }

    private void DestroyCurrentlyDisplayedPrefab() {
        Destroy(GameObject.Find("PrefabCanvas").GetComponent<GameObject>());
    }

    public int GetCategoryNumOffset() {
        return numOfCategoriesBeingDisplayed * curCategoryPageNum;
    }

    public void SetNumOfCategoriesBeingDisplayed() {
        numOfCategoriesBeingDisplayed = 5;
    }

    public void NextCategoryPage() {
        ++curCategoryPageNum;
        UpdateCategoryCubeNames();
    }

    public void PreviousCategoryPage() {
        DecrementCurMnemonicNum();
        UpdateCategoryCubeNames();
    }

    private void DecrementCurMnemonicNum() {
        curMnemonicNum--;
        if (curMnemonicNum == -1)
            curMnemonicNum++;
    }

    public void DisplayCanvasRightButtonClicked() {
        Debug.Log("insideDisplayCanvasRightButtonClicked()");
        ClearDisplayArea();
        IncrementCurMnemonicNum();

        DisplayMnemonic();
    }

    private void IncrementCurMnemonicNum() {
        curMnemonicNum++;
        if (curMnemonicNum == prefabFileInfo0.Length)
            curMnemonicNum--;
    }

    public void DisplayCanvasLeftButtonClicked() {
        Debug.Log("insideDisplayCanvasLeftButtonClicked()");
        ClearDisplayArea();
        DisplayMnemonic();
    }

    private void UpdateCategoryCubeNames() {
        int numCategories = GetNumOfCategoriesInCurrentPage();
        if (numCategories > 4)
            GameObject.Find("CategoryCube4").GetComponentInChildren<Text>().text = categoryDirectory[GetCategoryNumOffset() + 4].Name;
        if (numCategories > 3)
            GameObject.Find("CategoryCube3").GetComponentInChildren<Text>().text = categoryDirectory[GetCategoryNumOffset() + 3].Name;
        if (numCategories > 2)
            GameObject.Find("CategoryCube2").GetComponentInChildren<Text>().text = categoryDirectory[GetCategoryNumOffset() + 2].Name;
        if (numCategories > 1)
            GameObject.Find("CategoryCube1").GetComponentInChildren<Text>().text = categoryDirectory[GetCategoryNumOffset() + 1].Name;
        if (numCategories > 0)
            GameObject.Find("CategoryCube0").GetComponentInChildren<Text>().text = categoryDirectory[GetCategoryNumOffset() + 0].Name;
    }

    private int GetNumOfCategoriesInCurrentPage() {
        return 4; //HARDCODED
    }

    public void SpawnGameObject() {
        GameObject spawnedGameObject = new GameObject();
        spawnedGameObject = currentlyDisplayedMenuGameObject;
        spawnedGameObject.transform.parent = null;
        GameObject.Find("DisplayAreaTitleText").GetComponent<Text>().text = "";
    }
}

/*
 * TODO: curMnemonicNum should not go out of bounds
 * TODO: HARDCODED 4
 * TODO: Emre android add a starting screen
 * TODO: IMPORTANT - must change the lighting back to real time or something
 * TODO: check performance and quality
 */

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