using UnityEngine;
using System.Collections;
using UnityEngine.VR;
using System.IO;

public class ObjectSpawner : MonoBehaviour
{
    public GameObject mnemonic; // populate the array in the Inspector
    public bool placeMnemonicMode;
    Vector3 mnemonicPositionVector;

    private Camera cam;
    private Vector3 positionHit;

    bool wasTouching = false;


    void Start(){
        placeMnemonicMode = true;
        cam = GetComponent< Camera > ();
    }

    public Vector3 getMnemonicPosition()
    {
        Ray ray = cam.ViewportPointToRay(new Vector3(0.5f, 0.5f, 0f));

        RaycastHit hit;

        if (Physics.Raycast(ray, out hit)) {
            Debug.DrawRay(ray.origin, ray.direction * 100, Color.red);
            print("I'm looking at " + hit.transform.name);
            mnemonicPositionVector = hit.point;
        }
        else
        {
            mnemonicPositionVector = new Vector3(-55.481f, 15.91f, -97.91f);
        }
        return mnemonicPositionVector;
    }
    public bool inRange(){
        return true;
    }
    public void placeMnemonic(){
        Vector3 forward = InputTracking.GetLocalRotation(VRNode.CenterEye) * cam.transform.forward;
        Vector3 spawnPos = cam.transform.position + forward * 2;
        GameObject.Instantiate(mnemonic, spawnPos, Quaternion.identity);
        // GameObject.Instantiate(mnemonic, getMnemonicPosition(), Quaternion.identity);

    }
    
    public void Update(){
        if (Input.touchCount > 0)
        {
            if (!wasTouching)
            {
                Debug.Log("Touched");
                placeMnemonic();
                wasTouching = true;
            }
        }
        else
        {
            wasTouching = false;
        }

        if (Input.GetKeyDown(KeyCode.Space))
        {

            if (placeMnemonicMode)
            {
                placeMnemonic();
            }
            //placeMnemonicMode = false; //todo: (hardcoded)
        }
    }
}
/*
 if (hit.rigidbody != null)
        {
            hit.rigidbody.AddForceAtPosition(ray.direction * pokeForce, hit.point);
        }
     */

//    Debug.Log("here: " + x.ToString() + y.ToString() + z.ToString());
//todo: spawn
//todo: write H He shit in the mnemonic
//todo: add rotate
