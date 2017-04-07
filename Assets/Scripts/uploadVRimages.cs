using Firebase;
using Firebase.Storage;
using Firebase.Unity.Editor;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class uploadVRimages : MonoBehaviour {

	private string logText = "";


	const int kMaxLogSize = 16382;
	DependencyStatus dependencyStatus = DependencyStatus.UnavailableOther;

	bool wasTouching = false;
	// Use this for initialization


	void Start () {
		dependencyStatus = FirebaseApp.CheckDependencies();
		if (dependencyStatus != DependencyStatus.Available)
		{
			FirebaseApp.FixDependenciesAsync().ContinueWith(task => {
				dependencyStatus = FirebaseApp.CheckDependencies();
				if (dependencyStatus == DependencyStatus.Available)
				{
					InitializeFirebase();
				}
				else
				{
					Debug.LogError(
						"Could not resolve all Firebase dependencies: " + dependencyStatus);
				}
			});
		}
		else
		{
			InitializeFirebase();
		}
	}
	Firebase.Storage.StorageReference storage_ref;
	Firebase.Storage.StorageReference screenshot_ref;
	Firebase.Storage.FirebaseStorage storage;
	void InitializeFirebase()
	{
		FirebaseApp app = FirebaseApp.DefaultInstance;
		app.SetEditorDatabaseUrl("https://vr-one-4e3bb.firebaseio.com/");

		// Get a reference to the storage service, using the default Firebase App
		storage = Firebase.Storage.FirebaseStorage.DefaultInstance;

		// Create a storage reference from our storage service
		storage_ref = storage.GetReferenceFromUrl("gs://vr-one-4e3bb.appspot.com/");
		//Firebase.Storage.StorageReference images_ref = storage_ref.Child ("map");
		screenshot_ref = storage_ref.Child ("map/Screenshot.png");




		//items = new ArrayList();
	}

	public void uploadImageToFirebase(){
		string local_file = Application.persistentDataPath + "/Snapshots/screenshot.png";

		screenshot_ref.PutFileAsync (local_file).ContinueWith (task => {
			if(task.IsFaulted || task.IsCanceled){
				Debug.Log("isFaulted or IsCancelled: " + task.Exception.ToString());
			} else {

				Firebase.Storage.StorageMetadata metadata = task.Result;
				string download_url = metadata.DownloadUrl.ToString();
				Debug.Log("Finished Uploading...");
				Debug.Log("Download URL = " + download_url);
			}
		});
	}



	// Update is called once per frame
	public void Update(){
		if (Input.touchCount > 0)
		{
			if (!wasTouching)
			{
				//Debug.Log("Touched");

				uploadImageToFirebase();
				wasTouching = true;
			}
		}
		else
		{
			wasTouching = false;
		}

		if (Input.GetKeyDown(KeyCode.Space))
		{


				uploadImageToFirebase();

			//placeMnemonicMode = false; //todo: (hardcoded)
		}
	}

}
