(function () {

/**
 * Variables
 */
var username       = '';
var user_fullname = '';
var lon           = -75.76232;
var lat           = 39.67608;

/**
 * Initialize
 */
function init() {
  // Register event listeners
  $('login-btn').addEventListener('click', login);
  $('nearby-btn').addEventListener('click', loadNearbyRestaurants);
  $('fav-btn').addEventListener('click', loadFavoriteRestaurants);
  $('recommend-btn').addEventListener('click', loadRecommendedRestaurants);
}

function onLoginValid(result) {
  username = result.username;
  user_fullname = result.name;
	  
  var loginForm = $('login-form');
  var restaurantNav = $('restaurant-nav');
  var restaurantList = $('restaurant-list');
  var avatar = $('avatar');
  var welcomeMsg = $('welcome-msg');
  var logoutBtn = $('logout-link');
  $('logout-link').addEventListener('click', onLoginInvalid);

  welcomeMsg.innerHTML = 'Welcome, ' + user_fullname;

  showElement(restaurantNav);
  showElement(restaurantList);
  showElement(avatar);
  showElement(welcomeMsg);
  showElement(logoutBtn, 'inline-block');
  hideElement(loginForm);

  initGeoLocation();
}

function onLoginInvalid() {
  var loginForm = $('login-form');
  var restaurantNav = $('restaurant-nav');
  var restaurantList = $('restaurant-list');
  var avatar = $('avatar');
  var welcomeMsg = $('welcome-msg');
  var logoutBtn = $('logout-link');

  hideElement(restaurantNav);
  hideElement(restaurantList);
  hideElement(avatar);
  hideElement(logoutBtn);
  hideElement(welcomeMsg);
  
  showElement(loginForm);
}

function initGeoLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(onPositionUpdated, onLoadPositionFailed, {maximumAge: 60000});
    showLoadingMessage('Retrieving your location...');
  } else {
	onLoadPositionFailed();
  }
}

function onPositionUpdated(position) {
  lat = position.coords.latitude;
  lon = position.coords.longitude;
  console.log(lat + " " + lon);

  loadNearbyRestaurants();
}

function onLoadPositionFailed() {
  console.warn('navigator.geolocation is not available');
  getLocationFromIP();
}

function getLocationFromIP() {
  // Get location from http://ipinfo.io/json
  var url = 'http://ipinfo.io/json'
  var req = null;
  ajax('GET', url, req,
    function (res) {
      var result = JSON.parse(res);
      if ('loc' in result) {
        var loc = result.loc.split(',');
        lat = loc[0];
        lon = loc[1];
      } else {
        console.warn('Getting location by IP failed.');
      }
      loadNearbyRestaurants();
    }
  );
}

//-----------------------------------
//  Login
//-----------------------------------

function login() {
  var username = $('username').value;
  var password = $('password').value;
  //password = md5(username + md5(password));

  
  //The request parameters
  var url = './user/login';
  //var params = 'clientname=' + username + '&password=' + password;
  //var formData = {"clientname": username, "password": password};
  var req = JSON.stringify({username: username, password: password});

  //ajax('POST', url + '?' + params, req,
  ajax('POST', url, req,
    // successful callback
    function (res) {
      var result = JSON.parse(res);
      console.log("this is the result: ");
      console.log(result);
      // successfully logged in
      if (result.status === 'success') {
        //console.log("success");
    	onLoginValid(result);
      }
    },
    // error
    function () {
      showLoginError();
    }
  );
}

function showLoginError() {
  $('login-error').innerHTML = 'Invalid username or password';
}

function clearLoginError() {
	$('login-error').innerHTML = '';
}

// -----------------------------------
//  Helper Functions
// -----------------------------------

/**
 * A helper function that makes a navigation button active
 * 
 * @param btnId - The id of the navigation button
 */
function activeBtn(btnId) {
  var btns = document.getElementsByClassName('main-nav-btn');

  // deactivate all navigation buttons
  for (var i = 0; i < btns.length; i++) {
    btns[i].className = btns[i].className.replace(/\bactive\b/, '');
  }
  
  // active the one that has id = btnId
  var btn = $(btnId);
  btn.className += ' active';
}

function showLoadingMessage(msg) {
  var restaurantList = $('restaurant-list');
  restaurantList.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i> ' + msg + '</p>';
}

function showWarningMessage(msg) {
  var restaurantList = $('restaurant-list');
  restaurantList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-triangle"></i> ' + msg + '</p>';
}

function showErrorMessage(msg) {
  var restaurantList = $('restaurant-list');
  restaurantList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-circle"></i> ' + msg + '</p>';
}

/**
 * A helper function that creates a DOM element <tag options...>
 * 
 * @param tag
 * @param options
 * @returns
 */
function $(tag, options) {
  if (!options) {
    return document.getElementById(tag);
  }

  var element = document.createElement(tag);

  for (var option in options) {
    if (options.hasOwnProperty(option)) {
      element[option] = options[option];
    }
  }

  return element;
}

function hideElement(element) {
  element.style.display = 'none';
}

function showElement(element, style) {
  var displayStyle = style ? style : 'block';
  element.style.display = displayStyle;
}

/**
 * AJAX helper
 * 
 * @param method - GET|POST|PUT|DELETE
 * @param url - API end point
 * @param callback - This the successful callback
 * @param errorHandler - This is the failed callback
 */
function ajax(method, url, data, callback, errorHandler) {
  var xhr = new XMLHttpRequest();

  xhr.open(method, url, true);

  xhr.onload = function () {
	switch (xhr.status) {
	  case 200:
        console.log("2000000");
		callback(xhr.responseText);
		break;
	  case 403:
        console.log("4444403");
		onLoginInvalid();
		break;
	  case 401:
        console.log("44444401");
		errorHandler();
		break;
	}
  };

  xhr.onerror = function () {
    console.error("The request couldn't be completed.");
    errorHandler();
  };

  if (data === null) {
    xhr.send();
  } else {
    xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
    xhr.send(data);
  }
}

// -------------------------------------
//  AJAX call server-side APIs
// -------------------------------------

/**
 * API #1
 * Load the nearby restaurants
 * API end point: [GET] /restaurants/init
 */
function loadNearbyRestaurants() {
  console.log('loadNearbyRestaurants');
  activeBtn('nearby-btn');

  // The request parameters
  var url = './restaurants/init';
  //var params = 'username=' + username + '&lat=' + lat + '&lon=' + lon;
  var req = JSON.stringify({username:username, lat:lat, lon:lon});
  
  // display loading message
  showLoadingMessage('Loading nearby restaurants...');
  
  // make AJAX call
  //ajax('GET', url + '?' + params, req,
  ajax('POST', url, req,
    // successful callback
    function (res) {
      var restaurants = JSON.parse(res);
      //console.log(res);
      if (!restaurants || restaurants.length === 0) {
        showWarningMessage('No nearby restaurant.');
      } else {
        listRestaurants(restaurants);
      }
    },
    // failed callback
    function () {
      showErrorMessage('Cannot load nearby restaurants.');
    }  
  );
}

/**
 * API #2
 * Load favorite restaurants
 * API end point: [GET] /history/id (id is username here)
 */
function loadFavoriteRestaurants() {
  activeBtn('fav-btn');

  // The request parameters
  var url = './history/' + username;
  var req = JSON.stringify({});
  
  // display loading message
  showLoadingMessage('Loading favorite restaurants...');

  // make AJAX call
  ajax('GET', url, req,
    function (res) {
      var restaurants = JSON.parse(res);
      if (!restaurants || restaurants.length === 0) {
        showWarningMessage('No favorite restaurant.');
      } else {
        //console.log(restaurants);
        listRestaurants(restaurants);
      }
    },
    function () {
      showErrorMessage('Cannot load favorite restaurants.');
    }  
  );
}

/**
 * API #3
 * Load recommended restaurants
 * API end point: [GET] /recommendations/username
 */
function loadRecommendedRestaurants() {
  activeBtn('recommend-btn');

  // The request parameters
  var url = './recommendations/' + username;
  var req = JSON.stringify({});
  
  // display loading message
  showLoadingMessage('Loading recommended restaurants...');

  // make AJAX call
  ajax('GET', url, req,
    // successful callback
    function (res) {
      var restaurants = JSON.parse(res);
      if (!restaurants || restaurants.length === 0) {
        showWarningMessage('No recommended restaurant. Make sure you have favorites.');
      } else {
        listRestaurants(restaurants);
      }
    },
    // failed callback
    function () {
      showErrorMessage('Cannot load recommended restaurants.');
    } 
  );
}

/**
 * API #4
 * Toggle favorite (or visited) restaurants
 * 
 * @param business_id - The restaurant business id
 * 
 * API end point: [POST]/[DELETE] /history
 * request json data: { username: c41, favorite: business_ids }
 */
function changeFavoriteRestaurant(business_id) {
  // Check whether this restaurant has been favorite or not
  var li = $('restaurant-' + business_id);
  console.log("favorite");
  console.log(li.dataset.favorite);
  var favIcon = $('fav-icon-' + business_id);

  // The request parameters
  var url = './history';
  var req = JSON.stringify({
    username: username,
    businessId: business_id
});
  var method = li.dataset.favorite === "true" ? 'DELETE' : 'POST';
  console.log(method);

  ajax(method, url, req,
    // successful callback
    function (res) {
      //var result = JSON.parse(res);
      if (res) {
        console.log(res);
        if(li.dataset.favorite === "true") {
          li.dataset.favorite = "false";
        } else {
          li.dataset.favorite = "true";
        }
        favIcon.className = li.dataset.favorite === "true" ? 'fa fa-heart' : 'fa fa-heart-o';
      }
    }
  );
}

// -------------------------------------
//  Create restaurant list
// -------------------------------------

/**
 * List restaurants
 * 
 * @param restaurants - An array of restaurant JSON objects
 */
function listRestaurants(restaurants) {
  // Clear the current results
  var restaurantList = $('restaurant-list');
  restaurantList.innerHTML = '';


  console.log(restaurants[0]);
  for (var i = 0; i < restaurants.length; i++) {
    addRestaurant(restaurantList, restaurants[i]);
  }
}

/**
 * Add restaurant to the list
 * 
 * @param restaurantList - The <ul id="restaurant-list"> tag
 * @param restaurant - The restaurant data (JSON object)
 */
function addRestaurant(restaurantList, restaurant) {
  var business_id = restaurant.businessId;
  
  // create the <li> tag and specify the id and class attributes
  var li = $('li', {
    id: 'restaurant-' + business_id,
    className: 'restaurant'
  });
  
  // set the data attribute
  li.dataset.business = business_id;
  if(restaurant.favoriteStatus === "true") {
    li.dataset.favorite = true;
  } else {
    li.dataset.favorite = false;
  }

  // restaurant image
  li.appendChild($('img', {src: restaurant.imageUrl}));

  // section
  var section = $('div', {});
  
  // title
  var title = $('a', {href: restaurant.url, target: '_blank', className: 'restaurant-name'});
  title.innerHTML = restaurant.name;
  section.appendChild(title);
  
  // category
  var category = $('p', {className: 'restaurant-category'});
  category.innerHTML = 'Category: ' + restaurant.categories;//.join(', ');
  section.appendChild(category);

  // stars
  var stars = $('div', {className: 'stars'});
  for (var i = 1; i <= restaurant.stars; i++) {
    var star = $('i', {className: 'fa fa-star'});
    stars.appendChild(star);
  }

  if (('' + restaurant.stars).match(/\.5$/)) {
    stars.appendChild($('i', {className: 'fa fa-star-half-o'}));
  }

  section.appendChild(stars);

  li.appendChild(section);

  // address
  var address = $('p', {className: 'restaurant-address'});
  
  address.innerHTML = restaurant.fullAddress.replace(/,/g, '<br/>');
  li.appendChild(address);

  // favorite link
  var favLink = $('p', {className: 'fav-link'});
  
  favLink.onclick = function () {
    changeFavoriteRestaurant(business_id);
  };
  
  favLink.appendChild($('i', {
    id: 'fav-icon-' + business_id,
    className: restaurant.favoriteStatus === "true" ? 'fa fa-heart' : 'fa fa-heart-o'
  }));
  
  li.appendChild(favLink);

  restaurantList.appendChild(li);
}

init();

})();

// END