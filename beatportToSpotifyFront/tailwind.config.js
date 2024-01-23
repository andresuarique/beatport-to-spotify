/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      brightness: {
        25: '.25',
      },
      aspectRatio:{
        '1/4':'1 / 4',
      }
    },
    fontFamily:{
      'quicksand':['Quicksand','sans-serif']
    }
  },
  plugins: [],
}

