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

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.Gson;

/** Servlet that handles comments data.*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    private ArrayList<String> comments;
    static boolean initialized;

    static {
        initialized = false;
    }

    // Populate the comment section in the beginning with hard coded values
    private void initialize() {
        if (!initialized) {
            initialized = true;
        } else {
            return;
        }

        comments = new ArrayList<String>();
        comments.add("Ana ");
        comments.add("has ");
        comments.add("app les.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Check to see if the comments has been initialized already
        initialize();
        response.setContentType("text/html;");
        response.getWriter().println(comments);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get new comment and add it to the list
        String text = getParameter(request, "comment", "");
        comments.add(text);
        response.setContentType("text/html;");
        response.getWriter().println(comments);

        // Redirect back to the HTML page.
        response.sendRedirect("/index.html");
    }

    /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
