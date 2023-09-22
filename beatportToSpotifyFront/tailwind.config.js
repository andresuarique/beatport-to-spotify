/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      brightness: {
        25: '.25',
      }
    },
    fontFamily:{
      'quicksand':['Quicksand','sans-serif']
    }
  },
  plugins: [],
}

