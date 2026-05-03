# HealthTrack Global - Quick Start Guide

## 🚀 Getting Started (2 Minutes)

### Step 1: Access the Application
Open your browser and go to:
```
http://localhost:8080
```

### Step 2: Log Your First Meal

**Using Text Search:**
1. In the "📝 Log Your Meal" panel, type a food name
2. Example: `chicken` or `salmon` or `rice`
3. Click the "Search" button
4. Click on a suggestion
5. Adjust portion size if needed
6. Click "✅ Add All Meals"

**Using Voice (Chrome/Edge recommended):**
1. Click the 🎤 microphone button
2. Say: "Chicken with rice and salad"
3. Food will auto-populate
4. Follow the same steps above

### Step 3: Watch Your Progress
- Check the "📊 Today's Progress" panel on the right
- See your calories, protein, carbs, and fat
- Visual progress bar shows progress toward your goal
- Update daily goal if you want (default: 2000 calories)

### Step 4: View Your Meals
- Scroll to "📋 Today's Meal Log" at the bottom
- See all logged meals with timestamps
- Delete meals with the "🗑️ Delete" button

---

## 🔑 Unlock AI Features (Optional)

### Get Your OpenAI API Key

1. Go to: https://platform.openai.com/api-keys
2. Sign in with your OpenAI account
3. Click "Create new secret key"
4. Copy the key (starts with `sk-`)

### Connect Your API Key

1. Scroll to the top of the app
2. Find "🔑 OpenAI API Configuration" section
3. Paste your API key in the input field
4. Click "Connect"
5. Wait for status to change to "✅ OpenAI Connected"

### Now You Can:
- Get AI-powered meal suggestions based on your preferences
- AI will analyze your ingredients and dietary preferences
- Get smart recommendations tailored to you

---

## 🎯 Common Tasks

### Change Daily Calorie Goal
1. Go to "📊 Today's Progress" panel
2. Find "Daily Goal (cal)" at the bottom
3. Enter new number (e.g., 2500)
4. Click away or press Enter
5. Progress bar updates instantly

### Set Food Preferences
1. Go to "⚙️ Preferences" panel
2. Click on your preference:
   - All Foods
   - 🥬 Vegetarian
   - 🌱 Vegan
   - 💪 High Protein
   - 📉 Low Carb
3. Updates your suggestions automatically

### Set Diet Status
Choose your current status:
- ✅ On Track
- ⚠️ Too Much Junk Food
- 💪 Need More Protein
- 💤 Undereating
- 😴 Too Heavy/Late

### Add Available Ingredients
1. Go to "⚙️ Preferences"
2. Scroll to "Home Ingredients"
3. Type ingredients separated by commas
4. Example: `Chicken, Rice, Broccoli, Eggs, Spinach`
5. AI will use these for better suggestions

---

## 📊 Understanding Your Dashboard

### Calories & Progress
- **Total Calories** - Sum of all meals today
- **Daily Goal** - Your target (default 2000)
- **Progress Bar** - Visual representation of progress
- **Percentage** - How close you are to your goal

### Nutrition Breakdown
- **Protein** - Muscle building (grams)
- **Carbs** - Energy source (grams)
- **Fat** - Essential for health (grams)

### Tips
- Aim for 30-40% protein for weight loss
- Balance carbs with vegetables
- Don't fear healthy fats
- Adjust goal based on your lifestyle

---

## 🎤 Voice Input Tips

### Best Practices
1. Speak clearly at normal volume
2. Say food items naturally: "chicken breast, 150 grams"
3. Browser will transcribe and search
4. Multiple items: "rice, salad, and salmon"
5. Include quantities when possible

### Supported Devices
- ✅ Chrome (best support)
- ✅ Edge (very good)
- ✅ Safari (good)
- ⚠️ Firefox (limited)
- ✅ Mobile browsers

### Requirements
- Microphone permission granted
- HTTPS (on production, HTTP ok locally)
- Quiet environment (optional but better)

---

## 🔍 Food Database

The app includes 60+ foods from around the world:

### Grains & Carbs
- White Rice, Brown Rice, Basmati Rice
- Noodles (Ramen, Chow Mein), Spaghetti
- Bread, Toast, Roti, Naan
- Oatmeal, Cereal, Pancakes

### Proteins
- Chicken (breast, thigh)
- Beef, Fish, Salmon, Tuna
- Eggs, Milk, Yogurt
- Paneer, Tofu, Lentils, Chickpeas

### Vegetables
- Broccoli, Spinach, Carrot, Tomato
- Onion, Potato, Sweet Potato
- Cucumber, Bell Pepper

### Fruits
- Banana, Apple, Orange, Grapes
- Mango, Watermelon, Berries

### Dairy & Fats
- Cheese, Butter, Olive Oil
- Nuts (Almonds, Peanuts)

### Indian Dishes
- Chicken Biryani, Dal, Butter Chicken
- Samosa, Dosa, Idli, Sambar

### And More!

---

## 💡 Smart Suggestions

The app generates 5 meal suggestions based on:
- Your food preference (vegetarian, vegan, etc.)
- Your diet status
- Available ingredients (if you add them)

**With OpenAI API:**
- AI learns your preferences
- More personalized recommendations
- Suggests meals based on ingredients you have

---

## ⚙️ Settings & Preferences

### Auto-Saved Settings
- Food preference (vegetarian, high-protein, etc.)
- Diet status (on track, undereating, etc.)
- Home ingredients list
- Daily calorie goal

### Where Data is Stored
- **Local:** H2 in-memory database
- **Auto-saved:** When you make changes
- **Persistence:** Lasts until app restarts
- **Production:** Switch to MySQL/PostgreSQL

---

## 🐛 Troubleshooting

### Meals Not Showing Up
1. Verify you clicked "✅ Add All Meals"
2. Check the Meal Log at the bottom
3. Refresh the page (F5)
4. Check browser console (F12) for errors

### Voice Not Working
1. Check microphone is connected
2. Grant microphone permission in browser
3. Try Chrome or Edge (best compatibility)
4. Use text search as alternative

### Progress Not Updating
1. Wait 5 seconds (auto-refresh)
2. Try adding another meal (triggers update)
3. Refresh the page
4. Check browser console for errors

### API Key Not Connecting
1. Verify key starts with `sk-`
2. Check you're using latest version
3. Ensure key is still active at openai.com
4. Try clearing and re-entering

---

## 🎓 Example Workflow

### Morning (Breakfast)
```
1. Click Search
2. Type: "oatmeal"
3. Select "Oatmeal"
4. Change portion to 200g
5. Click "Add All Meals"
6. Check: ~150 calories, 5g protein
```

### Lunch (Click Voice 🎤)
```
1. Click microphone
2. Say: "grilled chicken 200 grams with rice 150 grams"
3. Two items appear
4. Click "Add All Meals"
5. Check: ~413 calories, 50g protein
```

### Dinner (Text Input)
```
1. Type: "salmon"
2. Select "Salmon"
3. Type: "broccoli"
4. Select "Broccoli"
5. Type: "salad"
6. Select desired salad
7. Click "Add All Meals"
8. View total: ~1000+ calories for the day
```

### End of Day
- Check progress bar (hopefully 80-100%)
- Review macro breakdown
- Update tomorrow's goal if needed
- Adjust preferences for next day

---

## 📱 Mobile Usage

The app works great on mobile!

### Touch-Friendly
- Large buttons (easy to tap)
- Responsive layout
- Scrollable panels
- Works in landscape & portrait

### Mobile Tips
1. Use portrait mode for best experience
2. Voice recognition works on mobile
3. Swipe down to refresh
4. Tap suggestions to add meals

---

## 🔒 Privacy & Security

### Data Storage
- ✅ All data stored locally on your computer
- ✅ No data sent to external servers (except OpenAI if enabled)
- ✅ OpenAI API key stored only on your device
- ✅ No tracking or analytics

### When Using OpenAI
- Your OpenAI API key is kept private
- Only food preferences sent to OpenAI for suggestions
- No personal information shared
- Standard OpenAI privacy terms apply

---

## 📞 Need Help?

### Check These Resources
1. **Browser Console** - Press F12, check Console tab
2. **Application Logs** - File at `/tmp/healthtrack.log`
3. **API Test** - Try endpoints with curl commands
4. **Test Report** - See `TEST_REPORT.md` for detailed info

### Common Issues
- **Food not tracking?** Check if "Add All Meals" button was clicked
- **No suggestions?** Set your preferences first
- **Voice not working?** Try Chrome browser instead
- **Summary not updating?** Wait 5 seconds or add another meal

---

## 🎉 You're All Set!

Your HealthTrack Global app is ready to use:
- ✅ Add meals via search or voice
- ✅ Track calories and macros
- ✅ Set preferences and goals
- ✅ Get suggestions (enhanced with OpenAI)
- ✅ Monitor daily progress

**Start tracking now:** http://localhost:8080

Enjoy your health journey! 🍎🏃‍♂️💪
