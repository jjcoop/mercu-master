import * as React from "react";
import { useEffect, useState } from "react";
import { Button } from "@mui/material";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import SendIcon from "@mui/icons-material/Send";

import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});


export default function UpdateSupplierForm() {
  const [inputValue, setInputValue] = React.useState("");
  const [inputId, setInputId] = React.useState("");
  const [keyword, setKeyword] = useState("supplierProcurement");
  const [data, setData] = useState([]);

  const [supplier, setSupplier] = useState('');
  const [fname, setFname] = useState('');
  const [lname, setLname] = useState('');
  const [phone, setPhone] = useState('');
  const [email, setEmail] = useState('');
  const [position, setPosition] = useState('');

  const [open, setOpen] = React.useState(false);
  const [badOpen, setBadOpen] = React.useState(false);
  const handleClose = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    setOpen(false);
    setBadOpen(false);
  };


  const fetchData = () => {
    fetch(`http://localhost:8787/${keyword}`)
      .then((response) => response.json())
      .then((data) => setData(data._embedded.supplierList))
      .catch((err) => console.error(err));
  };

  useEffect(() => {
    setInterval(() => {
      fetchData();
    }, 1000);
  }, []);

  const handleSubmit = async (event) => {
    // Stop the form from submitting and refreshing the page.
    event.preventDefault();

    // Get data from the form.
    const data = {
      fname: event.target.firstName.value,
      lname: event.target.lastName.value,
      phone: event.target.contactPhone.value,
      email: event.target.contactEmail.value,
      position: event.target.contactPosition.value,
    };

    // Send the data to the server in JSON format.
    const JSONdata = JSON.stringify(data);

    // API endpoint where we send form data.

    const endpoint = `http://localhost:8787/supplierProcurement/${inputId}/contact`;

    // Form the request for sending data to the server.
    const options = {
      // The method is POST because we are sending data.
      method: "PUT",
      // Tell the server we're sending JSON.
      headers: {
        "Content-Type": "application/json",
      },
      // Body of the request is the JSON data we created above.
      body: JSONdata,
    };

    // Send the form data to our forms API on Vercel and get a response.
    const response = await fetch(endpoint, options);

    // Get the response data from server as JSON.
    // If server returns the name submitted, that means the form works.
    const result = await response.json();

    if (response.status == 201) {
      setOpen(true);
      setSupplier('');
      setFname('');
      setLname('');
      setPhone('');
      setEmail('');
      setPosition('');
    }
    else{
      setBadOpen(true);
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <Autocomplete
          inputValue={supplier}
          onChange={(e,v)=>setSupplier(v?.companyName||v)}
          getOptionLabel={(option) => `${option.companyName}: ${option.id}`}
          onInputChange={(event, newInputValue) => {
            setInputValue(newInputValue);
            setInputId(newInputValue.replace(/\D/g, ""));
          }}
          disablePortal
          id="combo-box-demo"
          options={data}
          sx={{ width: 400 }}
          renderInput={(params) => (
            <div>
              <TextField {...params} label="Select Supplier To Add Contact" />
              <br />
            </div>
          )}
        />
        <br />
        <TextField
          required
          id="outlined-required"
          label="First Name"
          name="firstName"
          onChange={event => setFname(event.target.value)}
          value={fname}
        />
        <TextField
          required
          id="outlined-required"
          label="Last Name"
          name="lastName"
          onChange={event => setLname(event.target.value)}
          value={lname}
        />
        <br />
        <TextField
          fullWidth
          margin="normal"
          required
          id="outlined-required"
          label="Phone"
          name="contactPhone"
          onChange={event => setPhone(event.target.value)}
          value={phone}
        />
        <TextField
          fullWidth
          margin="normal"
          required
          id="outlined-required"
          label="Email"
          name="contactEmail"
          onChange={event => setEmail(event.target.value)}
          value={email}
        />
        <TextField
          fullWidth
          margin="normal"
          required
          id="outlined-required"
          label="Position"
          name="contactPosition"
          onChange={event => setPosition(event.target.value)}
          value={position}
        />
        <br />
        <Button
          color="success"
          sx={{ width: 250, marginTop: 2 }}
          type="submit"
          variant="contained"
          endIcon={<SendIcon />}
        >
          Create Contact
        </Button>

        <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
        <Alert onClose={handleClose} severity="success" sx={{ width: '100%' }}>
          Success! New Contact Created!
        </Alert>
        </Snackbar>

        <Snackbar open={badOpen} autoHideDuration={6000} onClose={handleClose}>
        <Alert onClose={handleClose} severity="error" sx={{ width: '100%' }}>
          Failed to create a new contact!
        </Alert>
        </Snackbar>
      </form>
    </div>
  );
}
