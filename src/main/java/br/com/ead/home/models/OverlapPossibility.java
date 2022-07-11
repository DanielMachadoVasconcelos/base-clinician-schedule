package br.com.ead.home.models;

public enum OverlapPossibility {
  EQUALS,                     // Starts and ends at the same time as other
  CONTAINS,                   // Other is completely within this
  IS_CONTAINED,               // This is completely within other
  STARTS_BEFORE_ENDS_WITHIN,  // This starts before other and ends within it
  STARTS_WITHIN_ENDS_AFTER,   // This starts within other and ends after it
  NO_OVERLAP                  // This does not overlap other
}