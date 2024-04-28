# AuthController API Documentation

## GET /current-user
Retrieves the current user.

**Headers:**
- `x-auth`: The authentication token.

**Response:**
- The `User` object.

---

## POST /login
Logs in a user.

**Request Body:**
- `User` object.

**Response:**
- The logged in `User` object.

---

## GET /logout
Logs out the current user.

---

## POST /change-password
Changes the password of the current user.

**Request Body:**
- `ChangePasswordRequest` object.

**Response:**
- The `ActionEntity` object.

---

## POST /forgot-password
Sends a password recovery email to the user.

**Request Body:**
- `username`: The username of the user.

**Response:**
- The `ActionEntity` object.

---

## POST /reset-password/{token}
Resets the user's password.

**Path Parameters:**
- `token`: The password reset token.

**Request Body:**
- `newPassword`: The new password.

**Response:**
- The `ActionEntity` object.

---

# ProfileController API Documentation

## POST /api/profile/create
Creates a new profile.

**Request Body:**
- `Profile` object.

**Response:**
- The created `Profile` object.

---

# AppointmentController API Documentation

## POST /api/appointment/create
Creates a new appointment.

**Request Body:**
- `AppointmentDTO` object.

**Response:**
- The created `Appointment` object.

---

## GET /api/appointment/{id}
Retrieves a specific appointment by its ID.

**Path Parameters:**
- `id`: The ID of the appointment.

**Response:**
- The `Appointment` object.

---

## GET /api/appointment
Retrieves all appointments of a specific user.

**Query Parameters:**
- `userId`: The ID of the user.

**Response:**
- A list of `Appointment` objects.

---

## GET /api/appointment/user-schedule/{user_id}
Retrieves the schedule of a specific user.

**Path Parameters:**
- `user_id`: The ID of the user.

**Response:**
- A list of `Date` objects.

---

## DELETE /api/appointment/{id}
Deletes a specific appointment.

**Path Parameters:**
- `id`: The ID of the appointment.

**Response:**
- The `ActionEntity` object.

---

## PUT /api/appointment/update/{id}
Updates a specific appointment.

**Path Parameters:**
- `id`: The ID of the appointment.

**Request Body:**
- `AppointmentDTO` object.

**Response:**
- The `ActionEntity` object.

---

## DELETE /api/appointment/all/{userId}
Deletes all appointments of a specific user.

**Path Parameters:**
- `userId`: The ID of the user.

**Response:**
- The `ActionEntity` object.

---

# UserController API Documentation

## POST /api/users/createPatient
Creates a new patient.

**Request Body:**
- `Patient` object.

**Response:**
- The created `Patient` object.

---

## PUT /api/users/patient
Updates a patient.

**Request Body:**
- `PatientDTO` object.

**Response:**
- The updated `Patient` object.

---

## GET /api/users/{id}
Retrieves a specific user by its ID.

**Path Parameters:**
- `id`: The ID of the user.

**Response:**
- The `User` object.