// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

// Get all comments and print them on index.html
async function updateCommentSection() {
    const response = await fetch('/data');
    var commentList = await response.text();
    
    // Split the comment list in comments
    commentList = commentList.slice(1, -2);
    let comments = commentList.split(", ");
    
    // Add every comment to the DOM
    var i, node, text;
    for (i = 0; i < comments.length; i++) {
        node = document.createElement('li');
        text = document.createTextNode(comments[i]);
        node.appendChild(text);
        document.getElementById("commentSection").appendChild(node);
    }
}
