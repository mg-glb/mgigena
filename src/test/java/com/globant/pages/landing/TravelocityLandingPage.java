package com.globant.pages.landing;

import com.globant.pages.results.TravelocityResultsPage;

public interface TravelocityLandingPage {

  void flights();

  TravelocityLandingPage flightsAndHotels();

  void flyFrom(String departure);

  void flyTo(String arrival);

  void pickDepartureDate();

  TravelocityResultsPage search();

  void pickReturnDate(int i);
}
