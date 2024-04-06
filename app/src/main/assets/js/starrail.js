"use strict";

// Initial Setup
var canvas = document.querySelector("canvas");
var c = canvas.getContext("2d");

canvas.width = innerWidth;
canvas.height = innerHeight;

// Variables
var mouse = {
    x: innerWidth / 2,
    y: innerHeight / 2 - 80,
};

var colors = ["#00bdff", "#4d39ce", "#088eff"];

// Event Listeners
addEventListener("mousemove", function (event) {
    mouse.x = event.clientX;
    mouse.y = event.clientY;
});

addEventListener("resize", function () {
    canvas.width = innerWidth;
    canvas.height = innerHeight;

    init();
});

// Utility Functions
function randomIntFromRange(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}

function randomColor(colors) {
    return colors[Math.floor(Math.random() * colors.length)];
}

// Objects
function Particle(x, y, radius, color) {
    var _this = this;

    var distance = randomIntFromRange(50, 120);
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.color = color;
    this.radians = Math.random() * Math.PI * 2;
    this.velocity = 0.05;
    this.distanceFromCenter = {
        x: distance,
        y: distance,
    };
    this.prevDistanceFromCenter = {
        x: distance,
        y: distance,
    };
    this.lastMouse = { x: x, y: y };

    this.update = function () {
        var lastPoint = { x: _this.x, y: _this.y };
        // Move points over time
        _this.radians += _this.velocity;

        // Drag effect
        _this.lastMouse.x += (mouse.x - _this.lastMouse.x) * 0.05;
        _this.lastMouse.y += (mouse.y - _this.lastMouse.y) * 0.05;

        // Circular Motion
        _this.distanceFromCenter.x =
            _this.prevDistanceFromCenter.x + Math.sin(_this.radians) * 100;
        _this.distanceFromCenter.y =
            _this.prevDistanceFromCenter.x + Math.sin(_this.radians) * 100;

        _this.x =
            _this.lastMouse.x +
            Math.cos(_this.radians) * _this.distanceFromCenter.x;
        _this.y =
            _this.lastMouse.y +
            Math.sin(_this.radians) * _this.distanceFromCenter.y;

        _this.draw(lastPoint);
    };

    this.draw = function (lastPoint) {
        c.beginPath();
        c.strokeStyle = _this.color;
        c.lineWidth = _this.radius;
        c.moveTo(lastPoint.x, lastPoint.y);
        c.lineTo(_this.x, _this.y);
        c.stroke();
        c.closePath();
    };
}

// Implementation
var particles = undefined;
function init() {
    particles = [];

    for (var i = 0; i < 50; i++) {
        var radius = Math.random() * 2 + 1;
        particles.push(
            new Particle(
                canvas.width / 2,
                canvas.height / 2,
                radius,
                randomColor(colors)
            )
        );
    }
}

// Animation Loop
function animate() {
    requestAnimationFrame(animate);
    c.fillStyle = "rgba(255, 255, 255, 0.05)";
    c.fillRect(0, 0, canvas.width, canvas.height);

    particles.forEach(function (particle) {
        particle.update();
    });
}

init();
animate();