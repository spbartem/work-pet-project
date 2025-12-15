/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}"
  ],
  theme: {
    extend: {
      keyframes: {
        progress: {
          "0%": { left: "-40%", width: "40%" },
          "50%": { left: "20%", width: "60%" },
          "100%": { left: "100%", width: "40%" },
        },
      },
      animation: {
        progress: "progress 1.5s infinite ease-in-out",
      },
    },
  },
  plugins: [],
}

