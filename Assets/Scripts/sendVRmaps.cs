using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class sendVRmaps : MonoBehaviour {
     public int resWidth = 2550;
     public int resHeight = 3300;

     private bool takeHiResShot = false;

     public static string ScreenShotName(int width, int height) {
         return string.Format("{0}/screenshots/screen_{1}x{2}_{3}.png",
                              Application.dataPath,
                              width, height,
                              System.DateTime.Now.ToString("yyyy-MM-dd_HH-mm-ss"));
     }

     public void TakeHiResShot() {
         takeHiResShot = true;
     }
	void clicked() {
		Debug.Log ("Hello");
		var filename = "screenshot.png";
		var path = Application.persistentDataPath + "/Snapshots/" + filename;
		Application.CaptureScreenshot(path);
	}
    void LateUpdate() {
        Camera camera1 = GetComponent<Camera>();
         takeHiResShot |= Input.GetKeyDown("k");
         if (takeHiResShot) {
			clicked ();
             RenderTexture rt = new RenderTexture(resWidth, resHeight, 24);
             camera1.targetTexture = rt;
             Texture2D screenShot = new Texture2D(resWidth, resHeight, TextureFormat.RGB24, false);
            camera1.Render();
             RenderTexture.active = rt;
             screenShot.ReadPixels(new Rect(0, 0, resWidth, resHeight), 0, 0);
            camera1.targetTexture = null;
             RenderTexture.active = null; // JC: added to avoid errors
             Destroy(rt);
             byte[] bytes = screenShot.EncodeToPNG();
             string filename = ScreenShotName(resWidth, resHeight);
             System.IO.File.WriteAllBytes(filename, bytes);
             Debug.Log(string.Format("Took screenshot to: {0}", filename));
             takeHiResShot = false;
         }
     }

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}

