using System.IO;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Firebase.Storage;

public class ImageLoader : MonoBehaviour {

    public bool isImage = false;
    public int imageNum = 0;
    private string url;
    private string urlBase = "https://firebasestorage.googleapis.com/v0/b/vr-one-4e3bb.appspot.com/o/snippedImages%2F";

    private void Start() {
        // test using the wizard image
        // TODO: remove this
//        DownloadImage(urlBase +"testing121490830010343?alt=media&token=b6a5220a-616a-41b2-ab3d-8628645ed492");
		DownloadImage(urlBase + "testing121490918413351?alt=media&token=dbc91101-d9d9-4e13-907c-d936cff40e84");
    }

    public void DownloadImage(string url) {
        this.url = url;
      //  Debug.Log("DownloadCalled");
        StartCoroutine(GetImage());
    }
    
    public void incrementImageNum() {
        imageNum++;

     //   Debug.Log("DownloadCalled");
        Debug.Log(urlBase + imageNum.ToString() + ".PNG?alt=media");
        DownloadImage(urlBase + imageNum.ToString() + ".PNG?alt=media");
    }

    public void decrementImageNum()
    {
        imageNum--;

        Debug.Log("DownloadCalled");
        Debug.Log(urlBase + imageNum.ToString() + ".PNG?alt=media");
        DownloadImage(urlBase + imageNum.ToString() + ".PNG?alt=media");
    }

    IEnumerator GetImage() {
        // Start a download of the given URL
        WWW www = new WWW(url);

        // Wait for download to complete
        yield return www;

        if (isImage) {
            // set the texture of the image (for canvas)
            Image image = GetComponent<Image>();
            image.sprite = Sprite.Create(www.texture, new Rect(0, 0, www.texture.width, www.texture.height), new Vector2(0, 0));
        } 
        else {
            Debug.Log("ImageClicked");
            // set the texture of the material for the entire object
            Renderer renderer = GetComponent<Renderer>();
            renderer.material.mainTexture = www.texture;


            if (!www.error.ToString().Equals("404 Not Found")) //works for a stupid reason
            {
                Debug.Log("Not Found Yo!");
                imageNum = -1;
                //changeURL();
            }
            else
            {
                
            }

                
        }
    }
}
