// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

function setScreenshotUrl(url) {
  document.getElementById('target').src = url;
}
function callVROne(){
console.log();	
//var uploadTask = firebase.storage().ref().child('UnSnippedImages/' + "testing").put(blob);
window.open("https://vr-one-4e3bb.firebaseapp.com","_self");
//window.location.href = "www.vr-one-4e3bb.firebaseapp.com";
}

document.getElementById("sendTovrone").addEventListener("click", callVROne);