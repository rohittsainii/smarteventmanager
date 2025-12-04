# 📅 Smart Event Manager

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![Material Design](https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white)

**A comprehensive Android event management application built with native Android components**

[Features](#-features) • [Screenshots](#-screenshots) • [Installation](#-installation) • [Tech Stack](#-tech-stack) • [Usage](#-usage)

</div>

---

## 📖 About

Smart Event Manager is a feature-rich Android application designed to help users organize, track, and manage their events efficiently. Built using native Android components and following Material Design principles, this app demonstrates modern Android development practices including SQLite database operations, fragment management, and notification handling.

## ✨ Features

### Core Functionality
- 📝 **Event Management** - Create, read, update, and delete events with ease
- 🗄️ **SQLite Database** - Persistent local storage for all events
- 🔔 **Smart Reminders** - Customizable notifications before events
- ⭐ **Priority System** - Rate events with 1-5 star priority levels
- 📊 **Statistics Dashboard** - View event analytics and breakdowns
- 🔍 **Search & Filter** - Find events quickly by title or category

### UI Components
- 📱 **Modern Material Design** - Clean and intuitive interface
- 📑 **Fragment Navigation** - Smooth transitions between screens
- 🎨 **Dynamic Themes** - Day/Night mode support
- 📋 **RecyclerView** - Efficient list rendering
- 🎯 **Interactive Elements** - Rating bars, seek bars, checkboxes, and more

### Advanced Features
- 🔄 **Multiple View Types** - List view and statistics view
- 📤 **Share Events** - Share event details via any app
- ⏰ **Alarm Integration** - System-level reminder scheduling
- 🎨 **Priority Color Coding** - Visual priority indicators
- ✅ **Completion Tracking** - Mark events as completed

## 🖼️ Screenshots

| Main Screen | Add Event | Statistics |
|-------------|-----------|------------|
| Event list with tabs | Form with all inputs | Analytics dashboard |

| Event Details | Context Menu | Search |
|---------------|--------------|--------|
| Full event information | Edit/Delete/Share | Quick search functionality |

## 🚀 Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 26 or higher
- Java Development Kit (JDK) 11

### Steps

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/smart-event-manager.git
cd smart-event-manager
```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Let Android Studio sync Gradle files automatically
   - Wait for dependencies to download

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button (▶️) or press `Shift + F10`

## 🛠️ Tech Stack

### Core Technologies
- **Language**: Java
- **Min SDK**: API 26 (Android 8.0 Oreo)
- **Target SDK**: API 36
- **Database**: SQLite
- **Architecture**: MVC (Model-View-Controller)

### Android Components
| Component | Usage |
|-----------|-------|
| Activities | MainActivity, AddEventActivity, EventDetailActivity |
| Fragments | EventListFragment, StatisticsFragment |
| Layouts | LinearLayout, RelativeLayout, CoordinatorLayout, CardView |
| RecyclerView | Event list display |
| ViewPager2 | Swipeable tabs |
| SQLiteOpenHelper | Database management |
| AlarmManager | Reminder scheduling |
| BroadcastReceiver | Notification handling |
| NotificationCompat | Cross-version notifications |

### UI Components Implemented
- ✅ Progress Bar
- ✅ Rating Bar (Priority)
- ✅ Seek Bar (Reminder time)
- ✅ Spinner (Category dropdown)
- ✅ Checkbox (Completion status)
- ✅ Switch/Toggle (Reminder on/off)
- ✅ SearchView
- ✅ Date Picker
- ✅ Time Picker
- ✅ FloatingActionButton (FAB)
- ✅ Toolbar with Menus
- ✅ Context Menu (Long press)
- ✅ Dialog Boxes (Alert, Confirmation)
- ✅ Toast Messages
- ✅ TabLayout

### Dependencies
```gradle
dependencies {
    implementation 'androidx.appcompat:appcompat:1.7.1'
    implementation 'com.google.android.material:material:1.13.0'
    implementation 'androidx.activity:activity:1.12.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.2.1'
}
```

## 📱 Usage

### Adding an Event
1. Tap the **Floating Action Button** (+) on the main screen
2. Fill in event details:
   - **Title** (required)
   - **Description** (optional)
   - **Date & Time** (tap to select)
   - **Category** (dropdown selection)
   - **Priority** (1-5 stars, required)
   - **Reminder** (toggle on/off, adjust minutes)
3. Tap **Save Event**

### Managing Events
- **View Details**: Tap any event card
- **Edit/Delete**: Long press → Select option
- **Mark Complete**: Check the checkbox on event card
- **Share**: Long press → Share option

### Search & Filter
- **Search**: Tap search icon in toolbar
- **Filter**: Menu → Filter → Select category

### View Statistics
- Swipe to **Statistics** tab
- View total, completed, and upcoming events
- See category and priority breakdowns

## 🗂️ Project Structure

```
smarteventmanager/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/smarteventmanager/
│   │   │   │   ├── Event.java                    # Model class
│   │   │   │   ├── EventDatabaseHelper.java      # SQLite database
│   │   │   │   ├── MainActivity.java             # Main activity
│   │   │   │   ├── AddEventActivity.java         # Add/Edit events
│   │   │   │   ├── EventDetailActivity.java      # Event details
│   │   │   │   ├── EventListFragment.java        # Event list
│   │   │   │   ├── StatisticsFragment.java       # Statistics
│   │   │   │   ├── EventAdapter.java             # RecyclerView adapter
│   │   │   │   ├── ViewPagerAdapter.java         # Tab adapter
│   │   │   │   └── NotificationReceiver.java     # Notification handler
│   │   │   ├── res/
│   │   │   │   ├── layout/                       # XML layouts
│   │   │   │   ├── menu/                         # Menu resources
│   │   │   │   ├── values/                       # Strings, colors, styles
│   │   │   │   └── mipmap/                       # App icons
│   │   │   └── AndroidManifest.xml
│   └── build.gradle
└── build.gradle
```

## 🎯 Key Features Demonstrated

This project showcases implementation of:

### Database Operations
- ✅ **CRUD Operations** - Create, Read, Update, Delete
- ✅ **Complex Queries** - Search, filter, sort
- ✅ **Data Persistence** - SQLite database

### UI/UX Design
- ✅ **Material Design** - Modern Android UI
- ✅ **Responsive Layouts** - Works on all screen sizes
- ✅ **Custom Themes** - Day/Night mode
- ✅ **Smooth Animations** - Fragment transitions

### Android Components
- ✅ **Intents** - Explicit and Implicit
- ✅ **Fragments** - Modular UI components
- ✅ **Services** - Background operations
- ✅ **Broadcast Receivers** - System events
- ✅ **Notifications** - Rich notifications with actions

## 🔧 Configuration

### Permissions Required
```xml
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
```

### Build Configuration
```gradle
android {
    compileSdk 36
    defaultConfig {
        applicationId "com.example.smarteventmanager"
        minSdk 26
        targetSdk 36
        versionCode 1
        versionName "1.0"
    }
}
```

## 🐛 Known Issues

- Edit functionality in EventDetailActivity is not yet implemented
- Notification permissions need manual approval on Android 13+
- Some devices may require exact alarm permissions

## 🔮 Future Enhancements

- [ ] Cloud sync with Firebase
- [ ] Calendar integration
- [ ] Event location with Google Maps
- [ ] Recurring events
- [ ] Event categories customization
- [ ] Export/Import events
- [ ] Widget support
- [ ] Dark theme improvements

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your Name](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

## 🙏 Acknowledgments

- [Material Design](https://material.io/design) for UI guidelines
- [Android Developers](https://developer.android.com/) for comprehensive documentation
- Stack Overflow community for problem-solving support

## 📞 Support

If you have any questions or need help, please:
- Open an issue in the GitHub repository
- Contact via email
- Check the [Wiki](https://github.com/yourusername/smart-event-manager/wiki) for detailed guides

---

<div align="center">

**⭐ Star this repo if you find it helpful!**

Made with ❤️ using Android Studio

</div>
