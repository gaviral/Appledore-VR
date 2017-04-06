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
        DownloadImage(urlBase + "0.PNG?alt=media");
    }

    public void DownloadImage(string url) {
        this.url = url;
        //  Debug.Log("DownloadCalled");
        StartCoroutine(GetImage());
    }

    public void incrementImageNum() {
        imageNum = (imageNum + 1) % 5;

        //   Debug.Log("DownloadCalled");
        Debug.Log(urlBase + imageNum.ToString() + ".PNG?alt=media");
        DownloadImage(urlBase + imageNum.ToString() + ".PNG?alt=media");
    }

    public void decrementImageNum() {
        imageNum--;

        Debug.Log("DownloadCalled");
        Debug.Log(urlBase + imageNum.ToString() + ".PNG?alt=media");
        DownloadImage(urlBase + imageNum.ToString() + ".PNG?alt=media");
    }

    private IEnumerator GetImage() {
        // Start a download of the given URL
        WWW www = new WWW(url);

        // Wait for download to complete
        yield return www;

        if (isImage) {
            // set the texture of the image (for canvas)
            Image image = GetComponent<Image>();
            image.sprite = Sprite.Create(www.texture, new Rect(0, 0, www.texture.width, www.texture.height), new Vector2(0, 0));
        } else {
            Debug.Log("ImageClicked");
            // set the texture of the material for the entire object
            Renderer renderer = GetComponent<Renderer>();
            renderer.material.mainTexture = www.texture;
        }
    }
}