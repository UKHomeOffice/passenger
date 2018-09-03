function init() {
    let tracker = ScrollTracker();
    tracker.on({
        percentages: {
            every: [25]
        },
        elements: {
            each: ['#user', '#details', '#conditions', '#contact']
        }
    }, function(evt) {
        console.log(evt.data.label); // > "25%"
        gtag('event', evt.data.label, { 'event_category': 'Scroll Tracking', 'event_label': document.location.pathname });
    });
}

if (document.readyState !== 'loading') {
    init();
} else {
    document.addEventListener('DOMContentLoaded', init);
}