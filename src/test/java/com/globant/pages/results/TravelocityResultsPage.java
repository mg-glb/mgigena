package com.globant.pages.results;

import com.globant.pages.TravelocityReturnFlightPage;

public interface TravelocityResultsPage {

  boolean checkSort();

  boolean checkSelectButtonForAll();

  boolean checkFlightDurationForAll();

  boolean checkDetailsForAll();

  void sortByDistanceShortest();

  boolean checkSortedByDistance(int days);

  TravelocityReturnFlightPage selectFirstResult();

  String[] selectedDepartureAndArrivalTimes();

  String hotelTextTag();

  String roomTextTag();

  String flightTextTag();

  String titleTextTag();
}
