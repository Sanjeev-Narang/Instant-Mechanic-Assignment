// Firebase Cloud Functions - Your REST API (Backend = Firebase)
// Deploy with: firebase deploy --only functions
// This is what your Retrofit client hits: BaseUrl = https://us-central1-<project>.cloudfunctions.net/api/

const functions = require("firebase-functions");
const admin = require("firebase-admin");
const express = require("express");
const cors = require("cors");

admin.initializeApp();
const db = admin.firestore();
const app = express();

app.use(cors({ origin: true }));
app.use(express.json());

// GET /api/mechanics?service=oil&lat=&lng=
app.get("/mechanics", async (req, res) => {
  try {
    const snap = await db.collection("mechanics").get();
    const mechanics = snap.docs.map((doc) => ({ id: doc.id, ...doc.data() }));
    res.json(mechanics);
  } catch (e) {
    console.error(e);
    res.status(500).json({ error: e.message });
  }
});

// GET /api/mechanics/:id
app.get("/mechanics/:id", async (req, res) => {
  const doc = await db.collection("mechanics").doc(req.params.id).get();
  if (!doc.exists) return res.status(404).json({ error: "Not found" });
  res.json({ id: doc.id, ...doc.data() });
});

// Example: POST /api/requests (create service request)
app.post("/requests", async (req, res) => {
  const { mechanicId, userId, service } = req.body;
  if (!mechanicId || !service) return res.status(400).json({ error: "Missing fields" });
  const ref = await db.collection("requests").add({
    mechanicId, userId, service,
    status: "pending",
    createdAt: admin.firestore.FieldValue.serverTimestamp(),
  });
  res.status(201).json({ id: ref.id });
});

exports.api = functions.https.onRequest(app);
