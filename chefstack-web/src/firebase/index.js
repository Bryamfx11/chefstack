import { initializeApp } from 'firebase/app'
import {
  getAuth,
  GoogleAuthProvider,
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  signInWithPopup,
  signOut,
  onAuthStateChanged
} from 'firebase/auth'

// config publica de Firebase (override opcional por VITE_*)
const firebaseConfig = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY || 'AIzaSyCrg_L8pOnDix12rSg6Y0kmk4VcRZ2NlLE',
  authDomain: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN || 'chefstack-9d766.firebaseapp.com',
  projectId: import.meta.env.VITE_FIREBASE_PROJECT_ID || 'chefstack-9d766',
  storageBucket: import.meta.env.VITE_FIREBASE_STORAGE_BUCKET || 'chefstack-9d766.firebasestorage.app',
  messagingSenderId: import.meta.env.VITE_FIREBASE_SENDER_ID || '99563627556',
  appId: import.meta.env.VITE_FIREBASE_APP_ID || '1:99563627556:web:e506de7f298693ef2d64b1'
}

const app = initializeApp(firebaseConfig)
const auth = getAuth(app)
const googleProvider = new GoogleAuthProvider()

// fachada de auth para no acoplar el resto de la app al SDK
export const authService = {
  registrar: (email, password) => createUserWithEmailAndPassword(auth, email, password),
  ingresar: (email, password) => signInWithEmailAndPassword(auth, email, password),
  ingresarConGoogle: () => signInWithPopup(auth, googleProvider),
  salir: () => signOut(auth),
  observarSesion: (callback) => onAuthStateChanged(auth, callback),
  usuarioActual: () => auth.currentUser
}
