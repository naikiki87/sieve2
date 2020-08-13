import axios from 'axios'

// module.exports = {
export default {
  pyrun : function() {
    var api = "http://localhost:3000/users/pyrun";
      axios
      .get(api)
      .then(response => {
        console.log(response);
      })
      .catch(err => { console.log(err); });
  }
}