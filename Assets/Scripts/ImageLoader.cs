using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ImageLoader : MonoBehaviour {

    public bool isImage = false;

    private string url;

    private void Start() {
        // test using the wizard image
        // TODO: remove this
        DownloadImage("https://firebasestorage.googleapis.com/v0/b/vr-one-4e3bb.appspot.com/o/waizrd.jpg?alt=media&token=014e980c-ec77-4d5c-a631-60d679bb3905");
    }

    public void DownloadImage(string url) {
        this.url = url;
        StartCoroutine(GetImage());
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
            // set the texture of the material for the entire object
            Renderer renderer = GetComponent<Renderer>();
            renderer.material.mainTexture = www.texture;
        }
    }
}
