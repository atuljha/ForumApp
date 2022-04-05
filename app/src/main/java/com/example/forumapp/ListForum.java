package com.example.forumapp;

import java.util.List;

public class ListForum{
  public   List<Forum> forums;
  public String name;

  public List<Forum> getForums() {
    return forums;
  }

  public ListForum() {
  }

  public void setForums(List<Forum> forums) {
    this.forums = forums;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
