using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TestPlugin : MonoBehaviour {

	AndroidJavaObject jo;

	// Use this for initialization
	void Start () {
		AndroidJavaClass pluginClass = new AndroidJavaClass("com.adam.myplugin.My_Plugin");
		string updateText = pluginClass.CallStatic<string>("getMessage");
		Debug.Log(updateText);
		GetComponent<Text>().text = updateText;

		jo = new AndroidJavaObject ("com.adam.myplugin.My_Plugin");
	}
	
	// Update is called once per frame
	void Update () {
		GetComponent<Text>().text = jo.Call<int>("getNextNumber").ToString();
	}
}
