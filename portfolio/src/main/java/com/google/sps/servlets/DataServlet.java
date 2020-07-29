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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that handles comments data.*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Comment");

        // Obtain information form DataStore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        
        // Add every comment existent to the comment list
        ArrayList<Comment> comments = new ArrayList<Comment>();
        for (Entity commentEntity : results.asIterable()) {
            String text = (String) commentEntity.getProperty("text");
            String time = (String) commentEntity.getProperty("time");
            String date = (String) commentEntity.getProperty("date");
            String user = (String) commentEntity.getProperty("user");

            comments.add(new Comment(text, time, date, user));
        }

        // Print all the comments stored in DataStore
        response.setContentType("text/json;");
        response.getWriter().println(buildJsonFromList(comments));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Create a data base using DatastoreService
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        // Get new comment and add it to the list
        String text = getParameter(request, "comment", "");
        
        // Add new comment to data base
        Comment com = new Comment(text, "", "", "");        
        storeData(com, datastore);

        // Redirect back to the HTML page.
        response.sendRedirect("/index.html");
    }

    // Add a Comment entity to a DataStore
    private void storeData(final Comment comment, DatastoreService datastore) {
        Entity commentEntity = new Entity("Comment");

        commentEntity.setProperty("text", comment.getText());
        commentEntity.setProperty("time", comment.getTime());
        commentEntity.setProperty("date", comment.getDate());
        commentEntity.setProperty("user", comment.getUser());

        datastore.put(commentEntity);
    }

    // Converts a Comment object into a JSON
    private String convertCommentToJson(Comment comment) {
        Gson gson = new Gson(); 
        return gson.toJson(comment);
    }

    // Converts an array of Comment objects to a JSON array
    private String buildJsonFromList(final ArrayList<Comment> list) {
        Gson gson = new Gson(); 
        return gson.toJson(list);
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
